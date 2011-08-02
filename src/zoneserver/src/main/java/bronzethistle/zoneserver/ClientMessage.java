package bronzethistle.zoneserver;

public class ClientMessage {
    private final String rawMessage;

    public ClientMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    @Override
    public String toString() {
        return rawMessage;
    }
}
