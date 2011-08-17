package bronzethistle.zoneserver.handlers.client;

import bronzethistle.zoneserver.Client;

public interface ClientMessageHandler<T> {
    public void handleMessage(Client client, T message);
}
