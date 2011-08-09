package bronzethistle.broker;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.config.impl.ConfigurationImpl;
import org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.core.server.HornetQServers;
import org.hornetq.core.server.JournalType;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Log4jConfigurer;
import org.springframework.util.SystemPropertyUtils;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hornetq.core.config.Configuration;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    private static final String[] APPLICATION_PACKAGES = {
            "bronzethistle.broker",
    };
    private static final String[] CONFIGURATIONS = {
            "file:${bronzethistle-broker.home}/conf/bronzethistle-broker.properties"
    };

    private static final String CONSOLE_LOGGER_CONFIG = "classpath:console-log4j.xml";
    private static final String FILE_LOGGER_CONFIG = "file:${bronzethistle-broker.home}/conf/log4j.xml";

    @Option(name = "-h", aliases = {"--home"}, usage = "The home directory of the application.", required = true)
    private String homePath;

    @Option(name = "-x", aliases = {"--log-to-console"}, usage = "Whether to log to system out/err and not to the logs.", required = false)
    private boolean logToConsole = false;

    private static App instance;
    private boolean running = false;
    private AbstractApplicationContext applicationContext;

    public static void main(String[] args) {
        instance = new App();
        CmdLineParser commandLineParser = new CmdLineParser(instance);
        commandLineParser.setUsageWidth(80);
        try {
            log.info("Starting bronzethistle broker...");
            commandLineParser.parseArgument(args);
            instance.start();
            log.info("broker started.");
            try {
                while (instance.isRunning()) {
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                log.info("shutting down...");
            }
            log.info("bronzethistle broker shut down.");
        } catch (CmdLineException e) {
            log.error("usage:");
            StringWriter usage = new StringWriter();
            commandLineParser.printUsage(usage, null);
            log.error(usage.toString());
        }
    }

    private void init() {
        System.setProperty("bronzethistle-broker.home", homePath);

        if (logToConsole) {
            try {
                Log4jConfigurer.initLogging(CONSOLE_LOGGER_CONFIG);
                log.info("Logging to console...");
            } catch (Throwable t) {
                throw new RuntimeException("Unable to initialize logging", t);
            }
        } else {
            try {
                Log4jConfigurer.initLogging(FILE_LOGGER_CONFIG);
            } catch (Throwable t) {
                throw new RuntimeException("Unable to initialize logging", t);
            }
        }
    }

    public synchronized void start() {
        if (running) {
            throw new RuntimeException("Server is already running.");
        }
        init();

        // create application context
        applicationContext = new AnnotationConfigApplicationContext();
        ((AnnotationConfigApplicationContext) applicationContext).scan(APPLICATION_PACKAGES);

        // add shut down hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                instance.stop();
            }
        });

        // add property configurer
        registerPropertyConfigurer(applicationContext);

        // start application context
        applicationContext.refresh();

        running = true;

        try {
            // HACK - start hornetq here first, once we get it figured out move it to a bean.
            Configuration configuration = new ConfigurationImpl();
            configuration.setPersistenceEnabled(true);
            configuration.setSecurityEnabled(false);
            Map<String, Object> connectionParams = new HashMap<String, Object>();
            connectionParams.put("port", 5445);
            configuration.getAcceptorConfigurations().add(new TransportConfiguration(NettyAcceptorFactory.class.getName(), connectionParams));
            configuration.setJournalType(JournalType.NIO);

            HornetQServer server = HornetQServers.newHornetQServer(configuration);
            server.start();



        } catch (Exception e) {
            log.error("failed to start hornetq", e);
        }
    }

    public synchronized void stop() {
        if (applicationContext != null) {
            applicationContext.close();
            applicationContext = null;
        }

        running = false;
    }


    /**
     * Registers a property configurer with the given application context.
     *
     * @param applicationContext
     */
    private void registerPropertyConfigurer(AbstractApplicationContext applicationContext) {
        List<Resource> locations = new ArrayList<Resource>();

        for (String configuration : CONFIGURATIONS) {
            locations.add(applicationContext.getResource(SystemPropertyUtils.resolvePlaceholders(configuration)));
        }

        final BeanDefinition definition = BeanDefinitionBuilder.rootBeanDefinition(PropertyPlaceholderConfigurer.class).addPropertyValue("locations", locations).getBeanDefinition();

        applicationContext.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor() {
            /**
             * @see org.springframework.beans.factory.config.BeanFactoryPostProcessor#postProcessBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
             */
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
                ((DefaultListableBeanFactory) beanFactory).registerBeanDefinition("propertyConfigurer", definition);
            }
        });
    }

    public boolean isRunning() {
        return running && applicationContext != null && applicationContext.isActive();
    }
}
