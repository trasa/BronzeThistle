package bronzethistle.zoneserver.handlers;

import bronzethistle.zoneserver.Client;

public interface GameMessageHandler<T> {
    public void handleMessage(Client client, T message);
}
