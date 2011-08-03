package bronzethistle.messages.client;


import java.util.Map;
import static com.google.common.collect.Maps.newHashMap;

public enum MessageType {
    LOGIN("login");

    private static final Map<String, MessageType> MESSAGE_TYPES = newHashMap();
    static {
        for(MessageType type : MessageType.values()) {
            MESSAGE_TYPES.put(type.code, type);
        }
    }

    private final String code;

    private MessageType(String code) {
        this.code = code;
    }

    public String getCode() { return code; }

    public static MessageType getMessageType(String code) {
        return MESSAGE_TYPES.get(code);
    }
}
