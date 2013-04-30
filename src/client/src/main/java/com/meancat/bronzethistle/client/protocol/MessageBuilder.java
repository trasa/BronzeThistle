package com.meancat.bronzethistle.client.protocol;

import bronzethistle.messages.client.LoginMessage;
import bronzethistle.messages.client.Message;
import bronzethistle.messages.client.MessageType;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.springframework.stereotype.Component;

/**
 * A totally brain dead text-to-Message implementation.
 */
@Component
public class MessageBuilder {

    public Message buildMessage(String s) {
        String[] parts = Iterables.toArray(Splitter.on(" ").trimResults().omitEmptyStrings().split(s), String.class);
        MessageType messageType = MessageType.getMessageType(parts[0]);
        if (messageType == null) {
            throw new RuntimeException("Unknown messageType: " + messageType);
        }
        switch(messageType) {
            case LOGIN:
                return new LoginMessage(parts[1]);
            default:
                throw new RuntimeException("Unknown messageType: " + messageType);
        }
    }
}
