package com.meancat.bronzethistle.roomserver;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 *
 */
public class RoomServer {
    public static final String PACKAGE_PATH = RoomServer.class.getPackage().getName().replace('.', '/');

    private static class ApplicationArguments {
        @Option(required=false, name="--home", aliases ={"-h"}, usage="Home Directory")
        public String home;

        @Option(required = false, name = "--zoo", aliases = { "-z" }, usage = "Zookeeper connection string.")
        public String zooConnectString;

        @Option(required = true, name = "--env", aliases = { "-e" }, usage = "Environment Name [dev,test]")
        public String environment;
    }

    private static AnnotationConfigWebApplicationContext applicationContext;

    public static void main(String[] args) throws Exception {
        ApplicationArguments appArgs = new ApplicationArguments();
        CmdLineParser parser = new CmdLineParser(appArgs);
        try {
            parser.parseArgument(args);
        } catch(CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println();
            parser.printUsage(System.err);
            return;
        }

        System.setProperty("app.environment", appArgs.environment);
        if (appArgs.home != null) {
            System.setProperty("app.home", appArgs.home);
        } else {
            System.setProperty("app.home", System.getProperty("user.dir"));
        }
        if (appArgs.zooConnectString != null) {
            System.setProperty("zoo.connectString", appArgs.zooConnectString);
        } else {
            StringBuilder zkBuilder = new StringBuilder();
            for(int ii = 1; ii <= 3; ii++) {
                zkBuilder.append(appArgs.environment);
                zkBuilder.append("-zk-00");
                zkBuilder.append(ii);
                zkBuilder.append(".bronzethistle.meancat..com:2181");
                zkBuilder.append(",");
            }
            System.setProperty("zoo.connectString", zkBuilder.length() > 0 ? zkBuilder.substring(0, zkBuilder.length() - 1): "");
        }

        configureLogging();
        System.out.println("Starting " + getDetails());

        applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(RoomServerConfiguration.class);
        applicationContext.refresh();
        applicationContext.registerShutdownHook();

        System.out.println("Started " + getDetails());

        while(applicationContext.isActive()) {
            Thread.sleep(5000);
        }
        ((LoggerContext) LoggerFactory.getILoggerFactory()).stop();
        System.out.println("Stopped " + getDetails());
    }



    private static void configureLogging() throws IOException {
        File localLogbackFile = new File(SystemPropertyUtils.resolvePlaceholders("${app.home}/logback.xml"));
        URL logbackUrl = localLogbackFile.toURI().toURL();

        if(!localLogbackFile.exists()) {
            String logbackPath = PACKAGE_PATH + "/conf/logback.xml";
            ClassPathResource logbackResource = new ClassPathResource(logbackPath);
            logbackUrl = logbackResource.getURL();
        }

        // setup logging
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            context.reset();
            configurator.doConfigure(logbackUrl);
        } catch (JoranException je) {
            // ignore since StatusPrinter will handle this
        }

        // fix JUL logging
        java.util.logging.Logger rootLogger = java.util.logging.LogManager.getLogManager().getLogger("");
        java.util.logging.Handler[] handlers = rootLogger.getHandlers();
        // noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < handlers.length; i++) {
            rootLogger.removeHandler(handlers[i]);
        }
        org.slf4j.bridge.SLF4JBridgeHandler.install();

        StatusPrinter.printInCaseOfErrorsOrWarnings(context);

        if(!new File("/.dockerinit").exists()) {
            System.out.println("Removing Loggly Appender: Not running in a docker container");
            Logger logger = context.getLogger(Logger.ROOT_LOGGER_NAME);
            logger.detachAppender("asyncLoggly");
        }

        System.out.println("Loaded Logback Configuration: " + logbackUrl.toExternalForm());
    }


    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * Gets details about the application.
     *
     * @return details
     */
    public static String getDetails() {
        return RoomServer.class.getSimpleName() + " - version: " + getVersion();
    }

    public static String getVersion() {
        String version = RoomServer.class.getPackage().getImplementationVersion();
        if (version == null)
            version = "(unpackaged deployment)";
        return version;
    }
}
