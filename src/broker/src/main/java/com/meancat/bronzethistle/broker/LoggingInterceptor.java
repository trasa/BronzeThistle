package com.meancat.bronzethistle.broker;

import java.util.Arrays;
import java.util.Map;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.Interceptor;
import org.hornetq.api.core.Message;
import org.hornetq.core.protocol.core.Packet;
import org.hornetq.core.protocol.core.impl.PacketImpl;
import org.hornetq.core.protocol.core.impl.wireformat.CreateQueueMessage;
import org.hornetq.core.protocol.core.impl.wireformat.CreateSessionMessage;
import org.hornetq.core.protocol.core.impl.wireformat.Ping;
import org.hornetq.core.protocol.core.impl.wireformat.SessionConsumerFlowCreditMessage;
import org.hornetq.core.protocol.core.impl.wireformat.SessionCreateConsumerMessage;
import org.hornetq.core.protocol.core.impl.wireformat.SessionQueueQueryMessage;
import org.hornetq.core.protocol.core.impl.wireformat.SessionRequestProducerCreditsMessage;
import org.hornetq.core.protocol.core.impl.wireformat.SessionSendMessage;
import org.hornetq.spi.core.protocol.RemotingConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;

import de.undercouch.bson4jackson.BsonFactory;

public class LoggingInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    private final ObjectMapper objectMapper;

    public LoggingInterceptor() {
        objectMapper = new ObjectMapper(new BsonFactory());
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Override
    public boolean intercept(org.hornetq.core.protocol.core.Packet packet, org.hornetq.spi.core.protocol.RemotingConnection remotingConnection) throws org.hornetq.api.core.HornetQException {
        try {
            Class<?> packetClass = packet.getClass();
            if (packetClass == CreateQueueMessage.class) {
                CreateQueueMessage m = (CreateQueueMessage) packet;
                logger.info("creating queue named " + m.getQueueName() + " at address " + m.getAddress());

            } else if (packetClass == SessionSendMessage.class) {
                interceptSessionSendMessage((SessionSendMessage) packet);

            } else if ((packetClass == Ping.class) ||
                    (packetClass == SessionQueueQueryMessage.class) ||
                    (packetClass == SessionConsumerFlowCreditMessage.class) ||
                    (packetClass == SessionRequestProducerCreditsMessage.class) ||
                    (packetClass == CreateSessionMessage.class) ||
                    (packetClass == PacketImpl.class) ||
                    (packetClass == SessionCreateConsumerMessage.class)) {
                // don't care.

            } else {
                logger.info("message interceptor called for packet type {} - {}",
                        packet.getClass().getName(), packet.toString());
            }

        } catch (Exception ex) {
            logger.error("LoggingInterceptor threw exception...ignoring.", ex);
        }
        return true;
    }

    private void interceptSessionSendMessage(SessionSendMessage packet) {
        Message msg = packet.getMessage();
        // if this is a shared-busprocessing message, use the type.
        String type = getType(msg);
        if (Strings.isNullOrEmpty(type)) {
            type = msg.getStringProperty("message_type");
        }
        if ("ss".equals(type)) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> serviceState = objectMapper.readValue(msg.getBytesProperty("body"), Map.class);
                logger.info("ServiceState from {} - {} userCount {}",
                        new Object[]{
                                serviceState.get("busAddress"),
                                serviceState.get("serviceType"),
                                serviceState.get("userCount")
                        });
            } catch(Exception ex) {
                logger.error("Failed to interpret ServiceState message", ex);
            }
        } else {
            logger.info("send message to {} : {}", msg.getAddress(), type);
        }
    }

    /*
     * below is copied from BusMessageType.java (in shared-busprocessing)
     */

    /**
     * Given a message, determine what it's message type is.
     *
     * @param message to examine
     * @return a type, or null if it can't be determined
     */
    private static String getType(Message message) {
        try {
            String s = message.getStringProperty("type");
            if (Strings.isNullOrEmpty(s)) {
                return null;
            }
            Integer i = Integer.valueOf(s);
            return intToString(i);
        } catch(NumberFormatException ex) {
            return null;
        }
    }

    private static String intToString(int a) {
        byte[] b = new byte[4];
        int count = 0;

        for (int i = 3; i > -1; i--) {
            byte bt = (byte) ((a >> (24 - (i * 8)) & 0xFF));

            if (bt > 0) {
                b[i] = bt;
                count++;
            } else {
                break;
            }
        }

        return new String(count == b.length ? b : Arrays.copyOfRange(b, b.length - count, b.length), Charsets.US_ASCII);
    }
}
