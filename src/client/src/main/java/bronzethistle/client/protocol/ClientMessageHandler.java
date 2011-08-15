package bronzethistle.client.protocol;

import bronzethistle.messages.client.Message;

public interface ClientMessageHandler<T> {
    void handleMessage(T msg);
}
