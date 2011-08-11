package bronzethistle.entitycontainer;

import bronzethistle.messages.entities.PlayerStats;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.SimpleString;
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


    public void registerEntity(String entityName, Object o) throws HornetQException {
        SimpleString address = new SimpleString(entityName);
        SimpleString queueName = new SimpleString(entityName + "." + identity);
        ClientSession.QueueQuery qq = session.queueQuery(queueName);
        if (qq == null || !qq.isExists()) {
            session.createTemporaryQueue(address, queueName);
        }


    }
}
