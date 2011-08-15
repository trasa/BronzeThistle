package bronzethistle.zoneserver;

import bronzethistle.messages.client.ZoneClientMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.google.common.collect.Maps.newConcurrentMap;

public class Zone {
    private static final Logger logger = LoggerFactory.getLogger(Zone.class);

    private final long zoneId;
    private String name;

    private ConcurrentMap<Long, Client> clients = newConcurrentMap();


    public Zone(long zoneId) {
        this.zoneId = zoneId;
    }

//    public void onEventConnect(ConnectMessage msg) {
//        logger.info("Player connected: user " + msg.getUserName());
//    }

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
        // TR TODO this needs to be rethought, see notes in notebook..

        logger.info(String.format("Client %s (%s) added to zone %s (%s)",
                client.getUserName(), client.getPlayerId(), name, zoneId));

        clients.put(client.getPlayerId(), client);

        // tell everybody about everybody.
        ZoneClientMessage msg = buildZoneClientMessage();
        for(Client c : clients.values()) {
            c.send(msg);
        }
    }

    private ZoneClientMessage buildZoneClientMessage() {
        ZoneClientMessage msg = new ZoneClientMessage();
        for(Client c : clients.values()) {
            msg.addClient(new ZoneClientMessage.ZoneClientRecord(c.getPlayerId(), c.getUserName()));
        }
        return msg;
    }

    public Client getClientById(long playerId) {
        return clients.get(playerId);
    }
}
