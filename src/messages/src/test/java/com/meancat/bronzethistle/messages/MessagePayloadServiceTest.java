package com.meancat.bronzethistle.messages;

import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertSame;

public class MessagePayloadServiceTest {

    private MessagePayloadService service;

    @Before
    public void setUp() {
        service = new MessagePayloadService();
    }

    @Test(expected = IllegalArgumentException.class)
    public void noArguments() {
        service.identifyMessagePayloadType(new Class<?>[0], new Annotation[0][0]);
    }

    @Test
    public void oneArgument() {
        Class<?> type = service.identifyMessagePayloadType(new Class<?>[] {String.class}, new Annotation[][]{ new Annotation[0]});
        assertSame(type, String.class);
    }

    @Test
    public void taggedWithAnnotation() throws NoSuchMethodException {
        Method m = this.getClass().getMethod("handlerMethod", String.class, Double.class, String.class);
        Class<?> type = service.identifyMessagePayloadType(m);
        assertSame(type, Double.class);
    }

    @Test
    public void takeLastOne() throws NoSuchMethodException {
        Method m = this.getClass().getMethod("handlerMethodNoAnnotation", Integer.class, Double.class, String.class);
        Class<?> type = service.identifyMessagePayloadType(m);
        assertSame(type, String.class);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void handlerMethod(String x1, @Payload Double payload, String x2) {
        // doesn't do anything... just for testing..
    }

    @SuppressWarnings("UnusedDeclaration")
    public void handlerMethodNoAnnotation(Integer x1, Double foo, String payload) {
        // doesn't do anything... just for testing...
    }
}
