package bronzethistle.messages.converters;


import bronzethistle.messages.client.*;
import bronzethistle.messages.protocol.SerializedClientMessage;
import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static bronzethistle.messages.client.MessageType.*;
import static com.google.common.collect.Lists.newArrayList;

@Component
public class MessageConverter {
    private static final Logger log = LoggerFactory.getLogger(MessageConverter.class);

    public Message deserialize(SerializedClientMessage rawMessage) {
        log.info(rawMessage.toString());
        ArrayList<String> parts = newArrayList(Splitter.on("|").omitEmptyStrings().trimResults().split(rawMessage.toString()));

        log.info("get type for " + parts.get(0));
        MessageType type = getMessageType(parts.get(0));

        if (type == null) {
            throw new UnknownMessageTypeException("Unknown message type: " + parts.get(0));
        }
        switch(type) {
            case LOGIN:
                return new LoginMessage(parts);
            case LOGIN_RESPONSE:
                return new LoginResponseMessage(parts);
            case ZONE_CLIENT:
                return new ZoneClientMessage(parts);
            default:
                throw new UnknownMessageTypeException("Unknown message type: " + parts.get(0));
        }
    }
}
