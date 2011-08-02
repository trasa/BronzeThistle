package bronzethistle.zoneserver;

import bronzethistle.messages.client.LoginMessage;
import org.hornetq.api.core.client.MessageHandler;
import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client implements MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private final Channel channel;
    private long playerId;
    private int channelId;
    public long currentZone;
    public String userName;

//    public final String outDestination;
//    public final String inDestination;


//    @Resource(name = "gameMessageHandlers")
//    protected Map<Integer, GameMessageHandler<?>> gameMessageHandlers;

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


    public void onMessage(org.hornetq.api.core.client.ClientMessage clientMessage) {
        // TODO
    }

    public void handleClientMessage(ClientMessage message) {
//        boolean sendToServer;
//        int messageId = -1;
//
//        try {
//            Object clientMessage = messageCodec.deserializeFromBytes(message);
//
//            messageId = messageCodec.getMessageIdByObject(clientMessage);
//
//            GameMessageHandler messageHandler = gameMessageHandlers.get(messageId);
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

    public Channel getChannel() {
        return channel;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }
}
