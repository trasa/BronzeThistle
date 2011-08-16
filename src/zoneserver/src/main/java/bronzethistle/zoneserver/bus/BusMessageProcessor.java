package bronzethistle.zoneserver.bus;


import bronzethistle.messages.client.Message;
import bronzethistle.messages.entities.PlayerStats;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.SimpleString;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Processes messages from a HornetQ bus.
 *
 * @author Elvir
 */
@Component
public class BusMessageProcessor {
    private static final Logger logger = LoggerFactory.getLogger(BusMessageProcessor.class);

    @Value("${hornetq.defaultMessageExpirationTime}")
    protected int defaultMessageExpirationTime;

    @Autowired
    protected ClientSession busSession = null;

    private ClientProducer producer = null;

    private Map<String, ClientConsumer> consumers = new ConcurrentHashMap<String, ClientConsumer>();

    /**
     * Initializes this processor.
     *
     * @throws org.hornetq.api.core.HornetQException
     *
     */
    @PostConstruct
    public void initialize() throws HornetQException {
        producer = busSession.createProducer();
    }

    /**
     * Closes this processor.
     *
     * @throws HornetQException
     */
    @PreDestroy
    public void close() throws HornetQException {
        if (producer != null) {
            producer.close();
        }
    }

    /**
     * Sets a consumer for the given destination.
     *
     */
    public void setConsumer(String entityAddress, MessageHandler messageHandler) throws HornetQException {
        SimpleString queueName = new SimpleString(entityAddress);

        ClientSession.QueueQuery queueQuery = busSession.queueQuery(queueName);
        if (queueQuery == null || !queueQuery.isExists()) {
            busSession.createQueue(new SimpleString(entityAddress), queueName, false);
        }

        ClientConsumer consumer = busSession.createConsumer(queueName);
        consumer.setMessageHandler(messageHandler);
        consumers.put(entityAddress, consumer);
    }

    /**
     * Removes a consumer from listening to a destination.
     *
     * @param destination
     */
    public void removeConsumer(String destination) throws HornetQException {
        ClientConsumer consumer = consumers.remove(destination);

        if (consumer != null)
            consumer.close();
    }

    /**
     * Sends a client message into the bus.
     *
     * @param destination
     * @param durable
     */
    public void sendMessage(String destination, String sender, boolean durable, int messageId, byte[] data) throws HornetQException {

        ClientMessage busMessage = busSession.createMessage(ClientMessage.MAP_TYPE, durable);

        busMessage.setExpiration(System.currentTimeMillis() + defaultMessageExpirationTime);

        busMessage.putIntProperty("message_type_id", messageId);
        busMessage.putStringProperty("sender", sender);
        busMessage.putBytesProperty("message", data);

        producer.send(destination, busMessage);
    }

    public void sendMessage(String address, Message msg) throws HornetQException {
        ClientMessage clientMessage = busSession.createMessage(false);
        clientMessage.putStringProperty("message_type", msg.getMessageType().getCode());
        clientMessage.putObjectProperty("serialized_state", msg);
        producer.send(address, clientMessage);
    }
}