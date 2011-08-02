package bronzethistle.zoneserver;

public interface GameMessageHandler<T> {
    public void handleMessage(Client client, T message);
}
