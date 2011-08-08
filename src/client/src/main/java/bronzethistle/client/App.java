package bronzethistle.client;

import bronzethistle.client.gui.MainForm;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Log4jConfigurer;
import org.springframework.util.SystemPropertyUtils;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    private static final String[] APPLICATION_PACKAGES = {
            "bronzethistle.client",
            "bronzethistle.messages"
    };
    private static final String[] CONFIGURATIONS = {
            "file:${bronzethistle-client.home}/conf/bronzethistle-client.properties"
    };

    private AbstractApplicationContext applicationContext;

    @Option(name = "-h", aliases = {"--home"}, usage = "The home directory of the application.", required = true)
    private String homePath;

    public static void main(String[] args) throws Exception {
        App instance = new App();
        CmdLineParser commandLineParser = new CmdLineParser(instance);
        commandLineParser.setUsageWidth(80);
        log.info("Starting bronzethistle client...");
        commandLineParser.parseArgument(args);

        instance.start();
        // TODO remove this, it isnt needed (i think)
        while(true) {
            Thread.sleep(1000);
        }
    }

    private void start() throws Exception {
        System.setProperty("bronzethistle-client.home", homePath);
        Log4jConfigurer.initLogging("file:${bronzethistle-client.home}/conf/log4j.xml");

        applicationContext = new AnnotationConfigApplicationContext();
        ((AnnotationConfigApplicationContext) applicationContext).scan(APPLICATION_PACKAGES);

        registerPropertyConfigurer(applicationContext);

        // start application context
        applicationContext.refresh();

        // display UI...
        JFrame frame = new JFrame("bronzethistle.client.gui.MainForm");
        MainForm mainForm = applicationContext.getBean(MainForm.class);
        frame.setContentPane(mainForm.getContentPane());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800, 600);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        in.readLine();

//  for (; ; ) {
//            String line = in.readLine();
//            if (line == null) {
//                break;

//        Channel channel = applicationContext.getBean(Channel.class);

//        // Read commands from the stdin.
//        ChannelFuture lastWriteFuture = null;
//        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//        for (; ; ) {
//            String line = in.readLine();
//            if (line == null) {
//                break;
//            }
//
//            // Sends the received line to the server.
//            log.info("sending " + line);
//            lastWriteFuture = channel.write(new SerializedClientMessage(line));
//
//            // If user typed the 'bye' command, wait until the server closes
//            // the connection.
//            if (line.toLowerCase().equals("bye")) {
//                channel.getCloseFuture().awaitUninterruptibly();
//                break;
//            }
//        }

//        // Wait until all messages are flushed before closing the channel.
//        if (lastWriteFuture != null) {
//            lastWriteFuture.awaitUninterruptibly();
//        }

        // Close the connection.  Make sure the close operation ends because
        // all I/O operations are asynchronous in Netty.
//        channel.close().awaitUninterruptibly();
//
//        // Shut down all thread pools to exit.
//        bootstrap.releaseExternalResources();
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

     public final ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
