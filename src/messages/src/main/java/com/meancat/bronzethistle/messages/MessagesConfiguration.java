package com.meancat.bronzethistle.messages;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {MessagesConfiguration.class})
public class MessagesConfiguration {

    @Bean
    public ObjectMapper jsonObjectMapper() {
        ObjectMapper bean = new ObjectMapper(new JsonFactory());
        bean.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return bean;
    }
}
