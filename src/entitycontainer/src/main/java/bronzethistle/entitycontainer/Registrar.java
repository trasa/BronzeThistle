package bronzethistle.entitycontainer;

import bronzethistle.messages.entities.PlayerStats;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.SimpleString;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Registrar {
    private static final Logger log = LoggerFactory.getLogger(Registrar.class);

    @Autowired
    protected ClientSession session;

    private String identity;

    public Registrar() {
        identity = String.valueOf(new Random().nextInt(1000));
    }


    public ClientConsumer registerEntity(String entityName) throws HornetQException {
        SimpleString address = new SimpleString(entityName);
        SimpleString queueName = new SimpleString(entityName + "." + identity);
        ClientSession.QueueQuery qq = session.queueQuery(queueName);
        if (qq == null || !qq.isExists()) {
            session.createTemporaryQueue(address, queueName);
        }
        return session.createConsumer(queueName);
    }

    public void sendObject(String entityName, Object obj) throws HornetQException {
        SimpleString queueName = new SimpleString(entityName);
        log.info(String.format("sending entity %s", entityName));
        ClientProducer producer = session.createProducer(queueName);
        ClientMessage msg = session.createMessage(true);
        msg.putStringProperty("message_type", "entity_state");

        // hack
        PlayerStats ps = (PlayerStats)obj; // omg that's so terrible you have to be kidding me.
        msg.putIntProperty("serialized_state", ps.getXp());

        producer.send(queueName, msg);
    }

    public void requestObject(String entityName) throws HornetQException {
        SimpleString address = new SimpleString(entityName);
        log.info("requesting object named " + address);
        ClientProducer producer = session.createProducer(address);
        ClientMessage msg = session.createMessage(true);
        msg.putStringProperty("message_type", "get");
        producer.send(address, msg);

    }
}
