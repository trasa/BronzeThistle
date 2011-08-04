package bronzethistle.messages.client;

import java.util.ArrayList;

public class LoginMessage implements Message {

    private String userName;

    public LoginMessage(ArrayList<String> parts) {
        userName = parts.get(1);
    }


//    public MessageType getMessageType() {
//        return MessageType.LOGIN;
//    }

    public String getUserName() {
        return userName;
    }
}
