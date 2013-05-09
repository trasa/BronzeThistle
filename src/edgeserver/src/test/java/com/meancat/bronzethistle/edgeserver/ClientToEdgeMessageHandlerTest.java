package com.meancat.bronzethistle.edgeserver;

import com.meancat.bronzethistle.edgeserver.handlers.EdgeMessageHandlerRegistry;
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
    private EdgeMessageHandlerRegistry mockEdgeMessageHandlerRegistry;

    @Before
    public void setUp() {
        handler = new ClientToEdgeMessageHandler();
        handler.edgeMessageHandlerRegistry = mockEdgeMessageHandlerRegistry;
    }

    @Test
    public void handleEdgePayload() {
        Message m = new Message();
        m.payload = new ServerTimeRequest();

        handler.handleEdgeMessage(m);

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
