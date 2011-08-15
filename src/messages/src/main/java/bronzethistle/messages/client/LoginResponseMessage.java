package bronzethistle.messages.client;


import java.util.List;

public class LoginResponseMessage implements Message {

    private long playerId;
    private String userName;

    public LoginResponseMessage(long playerId, String userName) {
        this.playerId = playerId;
        this.userName = userName;
    }

    public LoginResponseMessage(List<String> parts) {
        this.playerId = Long.parseLong(parts.get(1));
        this.userName = parts.get(2);
    }

    public long getPlayerId() { return playerId; }
    public String getUserName() { return userName; }

    @Override
    public String toString() {
        return String.format("LoginResponse: %s - %s",
                playerId, userName);
    }
}
