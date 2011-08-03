package bronzethistle.messages.converters;


import bronzethistle.messages.client.Message;
import bronzethistle.messages.client.SerializedClientMessage;
import bronzethistle.messages.client.LoginMessage;
import org.springframework.stereotype.Component;

@Component
public class MessageConverter {
    public Message deserialize(SerializedClientMessage rawMessage) {
        // TODO
        return new LoginMessage();
    }
}
