package bronzethistle.zoneserver;

import bronzethistle.messages.client.Message;
import bronzethistle.messages.client.SerializedClientMessage;
import bronzethistle.zoneserver.handlers.GameMessageHandler;
import bronzethistle.messages.converters.MessageConverter;
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

    // TODO fields:
//    private long currentZone;
//    private String userName;
//    private final String outDestination;
//    private final String inDestination;

    @Autowired
    protected MessageConverter messageConverter;

    @Resource(name = "gameMessageHandlers")
    protected Map<String, GameMessageHandler<?>> gameMessageHandlers;

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

    public void handleClientMessage(SerializedClientMessage rawMessage) {
        Message msg = messageConverter.deserialize(rawMessage);
        GameMessageHandler messageHandler = gameMessageHandlers.get(msg.getClass().getName());
        if (messageHandler != null) {
            messageHandler.handleMessage(this, msg);
        } else {
            logger.info("message hanlder not found for " + msg.getClass().getName());
        }
        // else ... send to server... or something... TODO


        // --- genesis code follows ---
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

    public int getChannelId() {
        return channelId;
    }
}
