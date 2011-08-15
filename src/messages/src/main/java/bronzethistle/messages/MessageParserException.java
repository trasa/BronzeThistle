package bronzethistle.messages;

public class MessageParserException extends Exception {
    public MessageParserException(String s) { super(s); }
    public MessageParserException(String s, Throwable t) { super(s, t); }
}
