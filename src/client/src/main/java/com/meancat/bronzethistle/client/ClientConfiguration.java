package com.meancat.bronzethistle.client;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.SystemPropertyUtils;

@Configuration
@ComponentScan(basePackageClasses = {ClientConfiguration.class})
public class ClientConfiguration {


    @Bean
    public static PropertyPlaceholderConfigurer propertyConfigurer() {
        PropertyPlaceholderConfigurer bean = new PropertyPlaceholderConfigurer();
        bean.setLocation(new DefaultResourceLoader().getResource(SystemPropertyUtils.resolvePlaceholders("file:${app.home}/conf/config.properties")));
        bean.setSearchSystemEnvironment(true);
        bean.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
        return bean;
    }
}
