package com.meancat.bronzethistle.edgeserver;

import com.meancat.bronzethistle.messages.Message;
import com.meancat.bronzethistle.messages.edge.ServerTimeRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

public class ClientToEdgeMessageHandlerTest {

    private ClientToEdgeMessageHandler handler;

    @Mock
    private Object edgeMessageHandlers;

    @Before
    public void setUp() {
        handler = new ClientToEdgeMessageHandler();
//        handler.edgeMessageHandlers = edgeMessageHandlers;
    }

    @Test
    @Ignore
    public void handleEdgePayload() {
        Message m = new Message();
        m.payload = new ServerTimeRequest();

        handler.handle(m);

        Assert.fail("handlers aren't implemented yet.");
    }

    @Test
    @Ignore
    public void routeEdgeMessageToCorrectHandler() {

    }

    @Test
    @Ignore
    public void incomingMessageIsForwardedToBroker() {

    }
}
