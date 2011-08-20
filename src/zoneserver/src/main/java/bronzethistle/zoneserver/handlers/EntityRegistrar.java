package bronzethistle.zoneserver.handlers;

import bronzethistle.messages.client.RequestEntityMessage;
import bronzethistle.zoneserver.Client;
import bronzethistle.zoneserver.bus.BusMessageProcessor;
import org.hornetq.api.core.HornetQException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityRegistrar {

    private static final Logger log = LoggerFactory.getLogger(EntityRegistrar.class);

    @Autowired
    protected BusMessageProcessor messageProcessor;


    public void requestEntity(Client requestedFor, String requestedEntity) {
        RequestEntityMessage msg = new RequestEntityMessage();
        msg.setEntityId(requestedEntity);
        try {
            messageProcessor.sendMessage(requestedEntity, msg);
        } catch (HornetQException e) {
            log.error("failed to request entity", e);
        }
    }

    public void entityResponse(Client client, RequestEntityMessage message) {

    }
}
