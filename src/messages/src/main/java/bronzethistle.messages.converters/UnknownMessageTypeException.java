package bronzethistle.messages.converters;

public class UnknownMessageTypeException extends RuntimeException {
    public UnknownMessageTypeException(String msg) {
        super(msg);
    }

    public UnknownMessageTypeException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
