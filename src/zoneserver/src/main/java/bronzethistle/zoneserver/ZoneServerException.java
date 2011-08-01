package bronzethistle.zoneserver;

public class ZoneServerException extends RuntimeException {
    public ZoneServerException(String message) {
        super(message);
    }

    public ZoneServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
