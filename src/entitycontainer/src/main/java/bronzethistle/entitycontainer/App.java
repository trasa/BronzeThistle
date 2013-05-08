package bronzethistle.entitycontainer;


import bronzethistle.messages.entities.PlayerStats;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
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
import org.springframework.util.SystemPropertyUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class App {
     private static final Logger log = LoggerFactory.getLogger(App.class);

    private static final String[] APPLICATION_PACKAGES = {
            "bronzethistle.entitycontainer",
//            "bronzethistle.broker"
    };
    private static final String[] CONFIGURATIONS = {
            "file:${bronzethistle-entitycontainer.home}/conf/bronzethistle-entitycontainer.properties"
    };

    @Option(name = "-h", aliases = {"--home"}, usage = "The home directory of the application.", required = true)
    private String homePath;

    @Option(name = "-a", usage="if this is the author instance", required=false)
    private boolean isAuthor;


    private Registrar registrar;

    public static void main(String[] args) throws Exception {
        App instance = new App();
        CmdLineParser commandLineParser = new CmdLineParser(instance);
        commandLineParser.setUsageWidth(80);
        log.info("Starting bronzethistle entity container...");
        commandLineParser.parseArgument(args);

        instance.start();
    }

    private void start() throws Exception {
        System.setProperty("bronzethistle-entitycontainer.home", homePath);
//        Log4jConfigurer.initLogging("file:${bronzethistle-entitycontainer.home}/conf/log4j.xml");

        AbstractApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        ((AnnotationConfigApplicationContext) applicationContext).scan(APPLICATION_PACKAGES);

        registerPropertyConfigurer(applicationContext);

        // start application context
        applicationContext.refresh();

        registrar = applicationContext.getBean(Registrar.class);

        if (isAuthor) {
            doAuthorBehavior();
        } else {
            doReaderBehavior();
        }

//        EntityActionParser actionParser = applicationContext.getBean(EntityActionParser.class);
//
//        try {
//            while(true) {
//                InputStreamReader isr = new InputStreamReader(System.in);
//                BufferedReader br = new BufferedReader(isr);
//                String s = br.readLine();
//                EntityAction a = actionParser.parse(s);
//                a.execute();
//            }
//        }
//        catch(Exception ex) {
//            ex.printStackTrace();
//        }
    }

    @SuppressWarnings({"InfiniteLoopStatement"})
    private void doAuthorBehavior() {
        log.info("Author Behavior");
        // register a new entity
        PlayerStats ps = new PlayerStats();
        ps.setXp(10);

        try {
            ClientConsumer consumer = registrar.registerEntity("XYZ");
            // wait for messages regarding our registered PlayerStats object.
            for(;;) {
                ClientMessage msg = consumer.receive();
                String msgType = msg.getStringProperty("message_type");
                if (msgType.equals("get")) {
                    registrar.sendObject("XYZ", ps);

                } else if (msgType.equals("entity_state")) {
                    // the entity_state message we just sent.
                    log.info("saw an entity_state message");

                } else {
                    log.error("Unknown message in author: " + msgType);
                }


                // wait here for a bit (until user hits enter) and then change the state.
                InputStreamReader isr = new InputStreamReader(System.in);
                BufferedReader br = new BufferedReader(isr);
                br.readLine();
                ps.setXp(50);
                registrar.sendObject("XYZ", ps);
            }
        } catch (Exception e) {
            log.error("author error", e);
        }
    }

    private void doReaderBehavior() {
        log.info("Reader Behavior");
        // register interest in entity called "XYZ"
        try {
            ClientConsumer consumer = registrar.registerEntity("XYZ");
            // get PlayerStat object
            registrar.requestObject("XYZ");

            for(;;) {
                // watch object for changes and print them out as they happen.
                ClientMessage msg = consumer.receive();
                String msgType = msg.getStringProperty("message_type");
                if (msgType.equals("entity_state")) {
                    PlayerStats ps = new PlayerStats();
                    ps.setXp(msg.getIntProperty("serialized_state"));
                    log.info("Got a PlayerStat with xp " + ps.getXp());
                } else if (msgType.equals("get")) {
                    log.info("saw our own get message");
                } else {
                    log.error("Unknown message in reader: " + msgType);
                }
            }
        } catch (Exception e) {
            log.error("reader error", e);
        }


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
}
