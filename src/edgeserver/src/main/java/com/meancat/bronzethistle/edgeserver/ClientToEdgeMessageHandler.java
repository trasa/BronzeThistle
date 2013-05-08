package com.meancat.bronzethistle.edgeserver;

import com.meancat.bronzethistle.messages.Message;
import org.springframework.stereotype.Component;

@Component
public class ClientToEdgeMessageHandler {
    public void handle(Message incomingMessage) {
        // is this a message that the edge itself needs to deal with?
        // TODO

        // otherwise forward on to the broker, routing, etc.
        // TODO
    }
}
