package com.meancat.bronzethistle.edgeserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.SystemPropertyUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@ComponentScan(basePackageClasses = {EdgeServerConfiguration.class})
public class EdgeServerConfiguration {
    @Value("${server.externalHostname}")
    public String serverHostname;

    @Value("${netty.bindPort}")
    private int socketBindPort;

    @Bean
    public static PropertyPlaceholderConfigurer propertyConfigurer() {
        PropertyPlaceholderConfigurer bean = new PropertyPlaceholderConfigurer();

        bean.setLocation(new DefaultResourceLoader().getResource(SystemPropertyUtils.resolvePlaceholders("file:${app.home}/conf/config.properties")));
        bean.setSearchSystemEnvironment(true);
        bean.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);

        return bean;
    }

    @Bean
    public Map<String, String> serviceStateProperties() {
        ConcurrentHashMap<String, String> bean = new ConcurrentHashMap<String, String>();
        bean.put("hostname", serverHostname);
        bean.put("port", socketBindPort + "");
        return bean;
    }
}
