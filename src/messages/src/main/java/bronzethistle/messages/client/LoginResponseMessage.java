package bronzethistle.messages.client;


public class LoginResponseMessage implements Message {

    private long playerId;
    private String userName;

    public LoginResponseMessage(long playerId, String userName) {
        this.playerId = playerId;
        this.userName = userName;
    }

    public long getPlayerId() { return playerId; }
    public String getUserName() { return userName; }
}
