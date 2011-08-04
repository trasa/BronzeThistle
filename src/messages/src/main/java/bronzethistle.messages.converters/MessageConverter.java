package bronzethistle.messages.converters;


import bronzethistle.messages.client.Message;
import bronzethistle.messages.client.MessageType;
import bronzethistle.messages.protocol.SerializedClientMessage;
import bronzethistle.messages.client.LoginMessage;
import com.google.common.base.Splitter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static bronzethistle.messages.client.MessageType.*;
import static com.google.common.collect.Lists.newArrayList;

@Component
public class MessageConverter {
    public Message deserialize(SerializedClientMessage rawMessage) {
        ArrayList<String> parts = newArrayList(Splitter.on("|").omitEmptyStrings().trimResults().split(rawMessage.toString()));
        MessageType type = getMessageType(parts.get(0));

        if (type == null) {
            throw new UnknownMessageTypeException("Unknown message type: " + parts.get(0));
        }
        switch(type) {
            case LOGIN:
                return new LoginMessage(parts);
            default:
                throw new UnknownMessageTypeException("Unknown message type: " + parts.get(0));
        }
    }
}
