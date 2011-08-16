package bronzethistle.zoneserver.handlers;

import bronzethistle.messages.client.RequestEntityMessage;
import bronzethistle.zoneserver.Client;
import org.springframework.beans.factory.annotation.Autowired;

public class RequestEntityHandler implements GameMessageHandler<RequestEntityMessage> {

    @Autowired
    protected EntityRegistrar registrar;


    public void handleMessage(Client client, RequestEntityMessage message) {
        // client wants to register their interest in an object.
        String entityClientWants = message.getEntityId();
    }
}
