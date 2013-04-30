package com.meancat.bronzethistle.edgeserver;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

public class EdgeServer {

    private static class ApplicationArguments {
        @Option(required = true, name = "--home", aliases = {"-h"}, usage = "Home directory of application.")
        public String home;
    }

    public static void main(String[] args) throws Exception {
        ApplicationArguments appArgs = new ApplicationArguments();

        CmdLineParser parser = new CmdLineParser(appArgs);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println();
            parser.printUsage(System.err);
            return;
        }

        // set system variables
        System.setProperty("app.home", appArgs.home);

        // setup logging
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            context.reset();

            configurator.doConfigure(new File(SystemPropertyUtils.resolvePlaceholders("${app.home}/conf/logback.xml")));
        } catch (JoranException je) {
            // ignore since StatusPrinter will handle this
        }

        StatusPrinter.printInCaseOfErrorsOrWarnings(context);

        // start spring and jetty
        System.out.println("Starting " + EdgeServer.getDetails());
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(EdgeServerConfiguration.class);
        applicationContext.refresh();

        System.out.println("Started " + EdgeServer.getDetails());

        while(applicationContext.isActive()) {
            Thread.sleep(5000);
        }
        System.out.println("Stopped " + EdgeServer.getDetails());
    }

    public static String getDetails() {
        return EdgeServer.class.getSimpleName() + " - version: " + EdgeServer.class.getPackage().getImplementationVersion();
    }
}
