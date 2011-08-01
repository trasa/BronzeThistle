package bronzethistle.zoneserver;

public class ClientMessage {
    private long objectId;
    private byte[] messageContent;

    public ClientMessage(byte[] messageContent) {
        this(0, messageContent);
    }

    public ClientMessage(long objectId, byte[] messageContent) {
        this.objectId =  objectId;
        this.messageContent = messageContent;
    }

    public long getObjectId() {
        return objectId;
    }

    public byte[] getMessageContent() {
        return messageContent;
    }
}
