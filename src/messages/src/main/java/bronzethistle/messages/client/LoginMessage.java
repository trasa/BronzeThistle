package bronzethistle.messages.client;

public class LoginMessage implements Message {

    public MessageType getMessageType() {
        return MessageType.LOGIN;
    }
}
