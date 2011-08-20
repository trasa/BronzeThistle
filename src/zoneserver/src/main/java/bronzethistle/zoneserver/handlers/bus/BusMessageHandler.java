package bronzethistle.zoneserver.handlers.bus;

public interface BusMessageHandler<T> {
    public void handleMessage(Object o, T message);
}
