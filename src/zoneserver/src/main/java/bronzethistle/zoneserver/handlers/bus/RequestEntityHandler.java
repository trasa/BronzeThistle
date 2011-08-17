package bronzethistle.zoneserver.handlers.bus;

import bronzethistle.messages.client.RequestEntityMessage;
import bronzethistle.zoneserver.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RequestEntityHandler implements BusMessageHandler<RequestEntityMessage> {
    Logger log = LoggerFactory.getLogger(RequestEntityHandler.class);


    public void handleMessage(Client client, RequestEntityMessage message) {
        log.info("bus handling message " + message.getEntityId());
    }
}
