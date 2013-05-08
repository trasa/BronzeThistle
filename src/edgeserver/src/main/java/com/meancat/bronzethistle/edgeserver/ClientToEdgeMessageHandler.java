package com.meancat.bronzethistle.edgeserver;

import com.meancat.bronzethistle.messages.Message;
import org.springframework.stereotype.Component;

@Component
public class ClientToEdgeMessageHandler {

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
        // TODO
        // find the appropriate handler for incomingMessage.payload
        // go call that handler
    }
}
