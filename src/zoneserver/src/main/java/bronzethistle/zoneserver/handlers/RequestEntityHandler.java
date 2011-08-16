package bronzethistle.zoneserver.handlers;

import bronzethistle.messages.client.RequestEntityMessage;
import bronzethistle.zoneserver.Client;
import org.hornetq.api.core.HornetQException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestEntityHandler implements GameMessageHandler<RequestEntityMessage> {

    private static final Logger log = LoggerFactory.getLogger(RequestEntityHandler.class);

    public void handleMessage(Client client, RequestEntityMessage message) {
        // client wants to register their interest in an object.
        String entityClientWants = message.getEntityId();
        try {
            client.registerEntity(entityClientWants);
            client.requestEntity(entityClientWants);
        } catch (HornetQException e) {
            log.error("Failed to register or request entity", e);
        }
    }
}
