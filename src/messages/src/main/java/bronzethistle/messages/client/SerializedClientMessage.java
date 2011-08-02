package bronzethistle.messages.client;

public class SerializedClientMessage {
    private final String rawMessage;

    public SerializedClientMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    @Override
    public String toString() {
        return rawMessage;
    }
}
