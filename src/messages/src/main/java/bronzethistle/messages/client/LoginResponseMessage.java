package bronzethistle.messages.client;

import java.util.List;

public class LoginResponseMessage implements Message {

    private long playerId;
    private String userName;
    private long zoneId;

    public LoginResponseMessage(long playerId, String userName, long zoneId) {
        this.playerId = playerId;
        this.userName = userName;
        this.zoneId = zoneId;
    }

    public LoginResponseMessage(List<String> parts) {
        this.playerId = Long.parseLong(parts.get(1));
        this.userName = parts.get(2);
    }

    public long getPlayerId() { return playerId; }
    public String getUserName() { return userName; }
    public long getZoneId() { return zoneId; }

    @Override
    public String toString() {
        return String.format("LoginResponse: %s - %s in zone %s",
                playerId, userName, zoneId);
    }

    public MessageType getMessageType() {
        return MessageType.LOGIN_RESPONSE;
    }
}
