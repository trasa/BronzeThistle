package bronzethistle.messages.client;

import com.google.common.base.Joiner;

import java.util.List;

public class LoginResponseMessage implements Message {

    private long playerId;
    private String userName;

    public LoginResponseMessage(long playerId, String userName) {
        this.playerId = playerId;
        this.userName = userName;
    }

    public LoginResponseMessage(List<String> parts) {
        playerId = Integer.parseInt(parts.get(1));
        userName = parts.get(2);
    }

    public String serialize() {
        return Joiner.on("|").join(MessageType.LOGIN_RESPONSE.getCode(), playerId, userName);
    }
}
