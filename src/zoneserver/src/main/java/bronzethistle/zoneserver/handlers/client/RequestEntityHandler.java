package bronzethistle.zoneserver.handlers.client;

import bronzethistle.messages.client.RequestEntityMessage;
import bronzethistle.zoneserver.Client;
import bronzethistle.zoneserver.handlers.EntityRegistrar;
import org.hornetq.api.core.HornetQException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestEntityHandler implements ClientMessageHandler<RequestEntityMessage> {

    private static final Logger log = LoggerFactory.getLogger(RequestEntityHandler.class);

//    @Autowired
//    protected EntityRegistrar registrar;


    public void handleMessage(Client client, RequestEntityMessage message) {
        // client is requesting a copy of the entity at this address.
        String entityClientWantsCopyOf = message.getEntityId();
//        registrar.requestEntity(client, entityClientWantsCopyOf);
        try {
            // register interest
            client.registerEntity(entityClientWantsCopyOf);
            // request the current state
            client.requestEntity(entityClientWantsCopyOf);
        } catch (HornetQException e) {
            log.error("Failed to register or request entity", e);
        }
    }
}
