package com.meancat.bronzethistle.edgeserver;

import com.meancat.bronzethistle.edgeserver.handlers.EdgeMessageHandlerRegistry;
import com.meancat.bronzethistle.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ClientToEdgeMessageHandler {

    @Autowired
    protected EdgeMessageHandlerRegistry edgeMessageHandlerRegistry;

    ExecutorService executor = Executors.newCachedThreadPool();

    public void handle(Message incomingMessage) {
        // is this a message that the edge itself needs to deal with?
        if (incomingMessage.isEdgePayload()) {
            handleEdgeMessage(incomingMessage);
        } else {
            // otherwise forward on to the broker, routing, etc.
            // TODO
            // broker.send(incomingMessage);
        }
    }

    private void handleEdgeMessage(Message incomingMessage) {
        // find the appropriate handlers for incomingMessage.payload
        for(EdgeMessageHandlerRegistry.HandlerMethod method : edgeMessageHandlerRegistry.findHandlers(incomingMessage)) {

        }

    }
}
