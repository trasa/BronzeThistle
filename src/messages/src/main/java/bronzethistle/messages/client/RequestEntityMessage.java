package bronzethistle.messages.client;

public class RequestEntityMessage implements Message {
    private String entityId;

    public String getEntityId() { return entityId; }
    public void setEntityId(String value) { this.entityId = value; }
}
