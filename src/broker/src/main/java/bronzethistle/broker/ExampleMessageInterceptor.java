package bronzethistle.broker;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.Interceptor;
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
import org.hornetq.api.core.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMessageInterceptor implements Interceptor {
    private static final Logger log = LoggerFactory.getLogger(ExampleMessageInterceptor.class);

    public boolean intercept(Packet packet, RemotingConnection remotingConnection) throws HornetQException {


        Class<?> packetClass = packet.getClass();
        if (packetClass == CreateQueueMessage.class) {
            CreateQueueMessage m = (CreateQueueMessage)packet;
            log.info("creating queue named " + m.getQueueName());

        } else if (packetClass == SessionSendMessage.class) {
            SessionSendMessage m = (SessionSendMessage)packet;
            Message msg = m.getMessage();
            log.info(String.format("session send message to %s : %s",
                   msg.getAddress(), msg.getStringProperty("message_type")));

        } else if (packetClass == Ping.class ||
                packetClass == SessionQueueQueryMessage.class ||
                packetClass == SessionConsumerFlowCreditMessage.class ||
                packetClass == SessionRequestProducerCreditsMessage.class ||
                packetClass == CreateSessionMessage.class ||
                packetClass == PacketImpl.class ||
                packetClass == SessionCreateConsumerMessage.class) {
            // dont care.
        } else {
            log.info(String.format("message interceptor called for packet type %s - %s",
                    packet.getClass().getName(), packet.toString()));
        }

        // returning false aborts interceptor and target.
        return true;
    }
}
