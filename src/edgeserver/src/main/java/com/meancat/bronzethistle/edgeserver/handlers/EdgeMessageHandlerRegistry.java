package com.meancat.bronzethistle.edgeserver.handlers;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.SetMultimap;
import com.meancat.bronzethistle.messages.MessagePayloadService;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Maps EdgeMessage payloads to the methods that handle those payloads.
 */
@Component
public class EdgeMessageHandlerRegistry {

    @Autowired
    protected Reflections reflections;
    @Autowired
    protected MessagePayloadService messagePayloadService;

    SetMultimap<Class<?>, HandlerMethod> handlerMethods;

    @PostConstruct
    public void init(ApplicationContext context) {

        for(Method m : reflections.getMethodsAnnotatedWith(EdgeMessageHandler.class)) {
            // for each of these Methods, get their Payload type
            HandlerMethod hm = new HandlerMethod(context.getBean(m.getClass()), m);
            handlerMethods.put(messagePayloadService.identifyMessagePayloadType(m), hm);
            m.getParameterTypes();
        }
    }

    private static class HandlerMethod {
        private final Object instance;
        private final Method method;

        public HandlerMethod(Object instance, Method method) {
            if (instance == null) {
                throw new IllegalArgumentException("instance can't be null");
            }
            if (method == null) {
                throw new IllegalArgumentException("method can't be null");
            }
            this.instance = instance;
            this.method = method;
        }
    }
}
