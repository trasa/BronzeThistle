package com.meancat.bronzethistle.client;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import com.meancat.bronzethistle.client.gui.MainForm;
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
import org.springframework.util.SystemPropertyUtils;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);


    private static class ApplicationArguments {
        @Option(name = "-h", aliases = {"--home"}, usage = "The home directory of the application.", required = true)
        private String home;
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

        System.setProperty("app.home", appArgs.home);

        LoggerContext context = (LoggerContext)LoggerFactory.getILoggerFactory();
        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            context.reset();
            configurator.doConfigure(new File(SystemPropertyUtils.resolvePlaceholders("${app.home}/conf/logback.xml")));
        } catch(JoranException ignored) {}
        StatusPrinter.printInCaseOfErrorsOrWarnings(context);

        System.out.println("Starting " + App.getDetails());
        App instance = new App();
        instance.start();

    }

    private void start() throws Exception {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ClientConfiguration.class);

        // display UI...
        JFrame frame = new JFrame("com.meancat.bronzethistle.client.gui.MainForm");
        com.meancat.bronzethistle.client.gui.MainForm mainForm = applicationContext.getBean(MainForm.class);
        frame.setContentPane(mainForm.getContentPane());

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800, 600);
    }

    public static String getDetails() {
        return "BronzeThistle Client - version: " + App.class.getPackage().getImplementationVersion();
    }
}
