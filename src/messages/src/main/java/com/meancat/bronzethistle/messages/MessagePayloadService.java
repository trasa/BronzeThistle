package com.meancat.bronzethistle.messages;

import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
public class MessagePayloadService {
    /**
     * Given a handler method, identify the parameter type that describes the messages the handler takes care of.

     * @param handlerMethod a method
     * @return the type of message payload that this handles
     */
    public Class<?> identifyMessagePayloadType(Method handlerMethod) {
        return identifyMessagePayloadType(handlerMethod.getParameterTypes(), handlerMethod.getParameterAnnotations());

    }

    public Class<?> identifyMessagePayloadType(Class<?>[] parameterTypes, Annotation[][] parameterAnnotations) {
        if (parameterTypes.length == 0) {
            throw new IllegalArgumentException("must have at least one parameter to identify the type with!");
        }
        for (int i=0; i < parameterTypes.length; i++) {
            Annotation[] pa = parameterAnnotations[i];
            for(Annotation a : pa) {
                if (a.annotationType().equals(Payload.class)) {
                    return parameterTypes[i];
                }
            }
        }
        // otherwise just return the last one.
        return parameterTypes[parameterTypes.length - 1];
    }
}
