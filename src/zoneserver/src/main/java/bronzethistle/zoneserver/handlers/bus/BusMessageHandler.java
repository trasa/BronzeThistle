package bronzethistle.zoneserver.handlers.bus;

import bronzethistle.zoneserver.Client;

public interface BusMessageHandler<T> {
    public void handleMessage(Client client, T message);
}
