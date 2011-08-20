package bronzethistle.zoneserver.handlers.bus;

import bronzethistle.messages.client.RequestEntityMessage;
import bronzethistle.zoneserver.Client;
import bronzethistle.zoneserver.handlers.EntityRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusRequestEntityHandler implements BusMessageHandler<RequestEntityMessage> {
    Logger log = LoggerFactory.getLogger(BusRequestEntityHandler.class);

//    @Autowired
//    protected EntityRegistrar registrar;

    public void handleMessage(Object client, RequestEntityMessage message) {
//        // Client received a request for an Entity.
//        log.info("bus handling message " + message.getEntityId());
//        registrar.entityResponse(client, message);
    }
}
