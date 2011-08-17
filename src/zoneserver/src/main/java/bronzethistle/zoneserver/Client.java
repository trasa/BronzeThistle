package bronzethistle.zoneserver;

import bronzethistle.messages.client.Message;
import bronzethistle.messages.client.RequestEntityMessage;
import bronzethistle.zoneserver.bus.BusMessageProcessor;
import bronzethistle.zoneserver.bus.MessageSerializer;
import bronzethistle.zoneserver.bus.SerializerException;
import bronzethistle.zoneserver.handlers.bus.BusMessageHandler;
import bronzethistle.zoneserver.handlers.client.ClientMessageHandler;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.MessageHandler;
import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Map;

public class Client implements MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private final Channel channel;
    private final long playerId;
    private final int channelId;
    private String userName;

    // TODO fields:
//    private long currentZone;
//    private final String outDestination;
//    private final String inDestination;

    @Resource(name = "gameMessageHandlers")
    protected Map<String, ClientMessageHandler<?>> gameMessageHandlers;

    @Resource(name = "busMessageHandlers")
    protected Map<String, BusMessageHandler<?>> busMessageHandlers;

    @Autowired
    protected BusMessageProcessor messageProcessor;

    /**
     * Creates a client attached to a channel.
     *
     * @param playerId
     * @param channel
     */
    public Client(long playerId, Channel channel) {
        this.playerId = playerId;
        this.channel = channel;
        this.channelId = channel.getId();

//        inDestination = "pi." + ConversionUtils.toUnsignedString(objectId);
//        outDestination = "po." + ConversionUtils.toUnsignedString(objectId);
    }


    /**
     * Initializes this client.
     */
    public void initialize() {
//        messageProcessor.setConsumer(outDestination, this);
    }

    /**
     * Closes this client.
     */
    public void close() {
//        messageProcessor.removeConsumer(outDestination);

//        if (currentZone != 0) {
//            DisconnectMessage message = new DisconnectMessage(objectId);

//            byte[] data = messageCodec.serializeToBytes(message);

//            messageProcessor.sendMessage("z." + ConversionUtils.toUnsignedString(currentZone), outDestination,
//                    false, messageCodec.getMessageIdByObject(message), data);
//        }
    }

    /**
     * This client wants to tell everybody that the client "owns" the entity at the address given.
     * The client will respond to others who want to get information about that entity.
     *
     * @param entityAddress
     * @throws HornetQException
     */
//    public void registerEntity(String entityAddress) throws HornetQException {
//        messageProcessor.setConsumer(entityAddress, this);
//    }

    /**
     * This client wishes to get a copy of the entity at the address given.
     * The client won't own the entity itself, just have a copy of it.
     * This only sends a request, it doesn't (by itself) fetch a copy.
     *
     * @param entityAddress
     * @throws HornetQException
     */
    public void requestEntity(String entityAddress) {
        // use registrar to get a copy?

        RequestEntityMessage msg = new RequestEntityMessage();
        msg.setEntityId(entityAddress);
        try {
            messageProcessor.sendMessage(entityAddress, msg);
        } catch (HornetQException e) {
            logger.error("failed to request entity", e);
        }
    }


    /**
     * A message received from the HornetQ broker.
     *
     * @param clientMessage
     */
    public void onMessage(ClientMessage clientMessage) {
        // TODO
        logger.info("Client.onMessage hornetq: " + clientMessage.getStringProperty("message_type"));
        try {
            Message msg = MessageSerializer.deserialize(clientMessage.getBytesProperty("serialized_state"));
            logger.info(String.format("received %s %s ", msg.getMessageType(), msg.getClass().getName()));

            // TODO dont handle this by class name, instead pull this apart so that there can be multiple handles for a given type of message.
            BusMessageHandler messageHandler = busMessageHandlers.get(msg.getClass().getName());
        if (messageHandler != null) {
            messageHandler.handleMessage(this, msg);
        } else {
            // else ... send to server... or something... TODO
            logger.info("bus message handler not found for " + msg.getClass().getName());
        }

        } catch (SerializerException e) {
            logger.error("failed to deserialize message", e);
        }
    }

    /**
     * A message received from the netty client.
     * @param msg
     */
    public void handleClientMessage(Message msg) {
        // TODO dont handle this by class name, instead pull this apart so that there can be multiple handles for a given type of message.
        ClientMessageHandler messageHandler = gameMessageHandlers.get(msg.getClass().getName());
        if (messageHandler != null) {
            messageHandler.handleMessage(this, msg);
        } else {
            // else ... send to server... or something... TODO
            logger.info("client message handler not found for " + msg.getClass().getName());
        }

        // --- genesis code follows ---
//        try {
//            Object clientMessage = messageCodec.deserializeFromBytes(message);
//
//            messageId = messageCodec.getMessageIdByObject(clientMessage);
//
//            ClientMessageHandler messageHandler = gameMessageHandlers.get(messageId);
//
//            if (messageHandler != null) {
//                sendToServer = messageHandler.handleMessage(this, clientMessage);
//            } else {
//                sendToServer = true;
//            }
//        } catch (Throwable t) {
//            logger.error("Unable to handle client message", t);
////            sendToServer = false;
//        }

//        if (sendToServer) {
//            messageProcessor.sendMessage(inDestination, outDestination, false, messageId, message.messageContent);
//        }
    }

    public long getPlayerId() { return playerId; }

    public int getChannelId() { return channelId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }


    /**
     * Send a Message to the player on the other side of this netty socket.
     *
     * @param message
     */
    public void send(Message message) {
        channel.write(message);
    }
}
