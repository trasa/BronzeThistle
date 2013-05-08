package com.meancat.bronzethistle.edgeserver.handlers;

import com.meancat.bronzethistle.messages.Message;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static junit.framework.Assert.assertTrue;

public class EdgeMessageHandlerRegistryTest {
    private EdgeMessageHandlerRegistry registry;

    @Before
    public void setUp() {
        registry = new EdgeMessageHandlerRegistry();
    }

    @Test
    public void ifNoHandlersReturnEmptySet() {
        Message m = new Message();
        m.payload = "this is a string payload";

        Set<EdgeMessageHandlerRegistry.HandlerMethod> handlers = registry.findHandlers(m);

        assertTrue(handlers.isEmpty());
    }

}
