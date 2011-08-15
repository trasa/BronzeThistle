package bronzethistle.messages.client;

public class RegisterObjectMessage implements Message {
    private String entityId;

    public String getEntityId() { return entityId; }
    public void setEntityId(String value) { this.entityId = value; }
}
