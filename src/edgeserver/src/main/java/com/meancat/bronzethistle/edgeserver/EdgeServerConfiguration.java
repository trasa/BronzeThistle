package com.meancat.bronzethistle.edgeserver;


import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
@ComponentScan(basePackageClasses = {EdgeServerConfiguration.class})
public class EdgeServerConfiguration extends WebMvcConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(EdgeServerConfiguration.class);

    @Value("${jetty.port}")
    public int jettyPort;

    @Value("${http.contentPath}")
    public Resource contentPath;

    @Bean
    public static PropertyPlaceholderConfigurer propertyConfigurer() {
        PropertyPlaceholderConfigurer bean = new PropertyPlaceholderConfigurer();

        bean.setLocation(new DefaultResourceLoader().getResource(SystemPropertyUtils.resolvePlaceholders("file:${app.home}/conf/config.properties")));
        bean.setSearchSystemEnvironment(true);
        bean.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);

        return bean;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server server(AbstractRefreshableWebApplicationContext applicationContext, SocketServlet socketServlet) throws IOException {
        Server server = new Server();

        Connector connector = new SelectChannelConnector();
        connector.setPort(jettyPort);
        server.addConnector(connector);

        server.setSendServerVersion(false);

        WebAppContext context = new WebAppContext(contentPath.getFile().getAbsolutePath(), "/");
        context.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
                applicationContext);

        context.addServlet(new ServletHolder(new DispatcherServlet(applicationContext)), "/mvc/*");
        context.addServlet(new ServletHolder(socketServlet), "/ws/*");

        server.setHandler(context);
        applicationContext.setServletContext(context.getServletContext());

        return server;
    }
}
