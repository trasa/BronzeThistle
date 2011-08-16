package bronzethistle.messages.client;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import java.util.List;

public class LoginMessage implements Message {

    private String userName;

    public LoginMessage(String userName) {
        this.userName = userName;
    }

    public LoginMessage(List<String> parts) {
        userName = parts.get(1);
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "LoginMessage: " + userName;
    }

    public MessageType getMessageType() {
        return MessageType.LOGIN;
    }
}
