package bronzethistle.messages;

import bronzethistle.messages.client.LoginMessage;
import bronzethistle.messages.client.LoginResponseMessage;
import bronzethistle.messages.client.Message;
import bronzethistle.messages.client.MessageType;
import bronzethistle.messages.client.ZoneClientMessage;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;

import static bronzethistle.messages.client.MessageType.*;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class MessageParser {

    private List<String> tokens;

    public MessageParser(String s) {
        tokens = Lists.newArrayList(Splitter.on(" ").trimResults().omitEmptyStrings().split(s));
    }

    public Message parse() throws MessageParserException {
        String messageTypeCode = tokens.get(0);
        MessageType type = getMessageType(messageTypeCode);
        if (type == null)
            throw new MessageParserException("Unknown Message Type: " + messageTypeCode);

        Message result;

        switch(type) {
            case LOGIN:
                result = new LoginMessage(tokens);
                break;
            case LOGIN_RESPONSE:
                result = new LoginResponseMessage(tokens);
                break;
            case ZONE_CLIENT:
                result = new ZoneClientMessage(tokens);
                break;
            default:
                throw new MessageParserException("Unknown Message Type: " + messageTypeCode);
        }
        return result;
    }
}
