package bronzethistle.zoneserver;

import bronzethistle.messages.client.Message;
import bronzethistle.zoneserver.bus.BusMessageProcessor;
import bronzethistle.zoneserver.bus.MessageSerializer;
import bronzethistle.zoneserver.bus.SerializerException;
import bronzethistle.zoneserver.handlers.bus.BusMessageHandler;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import static com.google.common.collect.Maps.newConcurrentMap;


public class Zone implements MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(Zone.class);

    private final long zoneId;
    private String name;

    private ConcurrentMap<Long, Client> clients = newConcurrentMap();

    @Autowired
    protected BusMessageProcessor messageProcessor;

    @Resource(name = "busMessageHandlers")
    protected Map<String, BusMessageHandler<?>> busMessageHandlers;

    public Zone(long zoneId) {
        this.zoneId = zoneId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getZoneId() {
        return zoneId;
    }

    public void addClient(Client client) {
        logger.info(String.format("Client %s (%s) added to zone %s (%s)",
                client.getUserName(), client.getPlayerId(), name, zoneId));

        clients.put(client.getPlayerId(), client);
    }

    public Client getClientById(long playerId) {
        return clients.get(playerId);
    }

    public void onMessage(ClientMessage clientMessage) {
        // extract message
        // TODO
        logger.info("Client.onMessage hornetq: " + clientMessage.getStringProperty("message_type"));
        try {
            Message msg = MessageSerializer.deserialize(clientMessage.getBytesProperty("serialized_state"));
            logger.info(String.format("received %s %s ", msg.getMessageType(), msg.getClass().getName()));

            // is this message for me to RESPOND to, or for me to READ and REACT to?
            // ex. if this message is a request for the object at my address, then I should send a copy of myself to whomever is asking.
            // or maybe it is an update message that i'm interested in and need to update my own state (and not necessarily send a state response)
            // or...something else, don't know.


//            // TODO dont handle this by class name, instead pull this apart so that there can be multiple handles for a given type of message.
//            BusMessageHandler messageHandler = busMessageHandlers.get(msg.getClass().getName());
//            if (messageHandler != null) {
//                messageHandler.handleMessage(this, msg);
//            } else {
//                // else ... send to server... or something... TODO
//                logger.info("bus message handler not found for " + msg.getClass().getName());
//            }

        } catch(Exception ex) {
            logger.error("zone onMessage failed", ex);
        }
    }
}
