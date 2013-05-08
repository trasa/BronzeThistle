package com.meancat.bronzethistle.edgeserver.handlers;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.SetMultimap;
import com.meancat.bronzethistle.messages.Message;
import com.meancat.bronzethistle.messages.MessagePayloadService;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Maps EdgeMessage payloads to the methods that handle those payloads.
 */
@Component
public class EdgeMessageHandlerRegistry {

    private static final Logger logger = LoggerFactory.getLogger(EdgeMessageHandlerRegistry.class);

    @Autowired
    protected Reflections reflections;
    @Autowired
    protected MessagePayloadService messagePayloadService;

    private SetMultimap<Class<?>, HandlerMethod> handlerMethods = LinkedHashMultimap.create();


    @PostConstruct
    public void init(ApplicationContext context) {
        for(Method m : reflections.getMethodsAnnotatedWith(EdgeMessageHandler.class)) {
            // for each of these Methods, get their Payload type
            HandlerMethod hm = new HandlerMethod(context.getBean(m.getClass()), m);
            handlerMethods.put(messagePayloadService.identifyMessagePayloadType(m), hm);
        }
    }

    public Set<HandlerMethod> findHandlers(Message message) {
        Set<HandlerMethod> methods = handlerMethods.get(message.getPayloadType());
        if (methods == null) {
            logger.warn("There are no handlerMethods registered for message of type {}!", message.getPayloadType());
            methods = new HashSet<HandlerMethod>();
        }
        return methods;
    }

    public static class HandlerMethod {
        public final Object instance;
        public final Method method;

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
