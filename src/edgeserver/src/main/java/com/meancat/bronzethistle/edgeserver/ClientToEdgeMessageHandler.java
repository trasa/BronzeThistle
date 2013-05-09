package com.meancat.bronzethistle.edgeserver;

import com.google.common.annotations.VisibleForTesting;
import com.meancat.bronzethistle.edgeserver.handlers.EdgeMessageHandlerRegistry;
import com.meancat.bronzethistle.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ClientToEdgeMessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(ClientToEdgeMessageHandler.class);

    @Autowired
    protected EdgeMessageHandlerRegistry edgeMessageHandlerRegistry;

    protected ExecutorService executor = Executors.newCachedThreadPool();

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

    @VisibleForTesting
    protected void handleEdgeMessage(Message incomingMessage) {
        // find the appropriate handlers for incomingMessage.payload
        for(EdgeMessageHandlerRegistry.HandlerMethod method : edgeMessageHandlerRegistry.findHandlers(incomingMessage)) {
            executor.submit(new EdgeMessageTask(incomingMessage));
        }
    }

    private class EdgeMessageTask implements Runnable {

        private Message incomingMessage;

        public EdgeMessageTask(Message incomingMessage) {
            this.incomingMessage = incomingMessage;
        }

        @Override
        public void run() {
            logger.debug("Running an Edge Message Task!");
            for(EdgeMessageHandlerRegistry.HandlerMethod m : edgeMessageHandlerRegistry.findHandlers(incomingMessage)) {
                try {
                    m.invoke(incomingMessage);
                } catch (Exception e) {
                    logger.error("Failed to invoke handler: " + m.toString() + " with message " + incomingMessage.toString(), e);
                }
            }
        }
    }
}
