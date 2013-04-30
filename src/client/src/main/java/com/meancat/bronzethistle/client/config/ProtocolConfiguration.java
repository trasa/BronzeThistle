package com.meancat.bronzethistle.client.config;

import com.meancat.bronzethistle.client.handlers.ClientMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@Configuration
public class ProtocolConfiguration {

    @Bean
    public Map<String, ClientMessageHandler<?>> clientMessageHandlers(List<ClientMessageHandler<?>> messageHandlers) {

        Map<String, ClientMessageHandler<?>> result = newHashMap();
        for (ClientMessageHandler<?> h : messageHandlers) {
            ParameterizedType paramType = (ParameterizedType)h.getClass().getGenericInterfaces()[0];
            Class<?> type = (Class<?>) paramType.getActualTypeArguments()[0];
            String key = type.getName();
            result.put(key, h);
        }
        return result;
    }
}
