package bronzethistle.messages.client;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newLinkedList;

public class ZoneClientMessage implements Message {
    private static final Logger log = LoggerFactory.getLogger(ZoneClientMessage.class);

    private List<ZoneClientRecord> clients = newArrayList();

    public ZoneClientMessage(List<String> parts) {

        for (int i=1; i < parts.size(); i++) {
            String r = parts.get(i);
            log.info("Zone Client Message: " + r);
            clients.add(new ZoneClientRecord(r));
        }
    }

    public ZoneClientMessage() {
    }

    public String serialize() {
        List<String> parts = newLinkedList();
        parts.add(MessageType.ZONE_CLIENT.getCode());
        for(ZoneClientRecord c : clients) {
            parts.add(c.toString());
        }
        return Joiner.on("|").join(parts);
    }

    public void addClient(ZoneClientRecord client) {
        clients.add(client);
    }

    public static class ZoneClientRecord implements Serializable {
        private final long playerId;
        private final String userName;

        public ZoneClientRecord(long playerId, String userName) {

            this.playerId = playerId;
            this.userName = userName;
        }

        public ZoneClientRecord(String serialized) {
            // TODO replace with something less stupid later.
            log.info("rec is '" + serialized + "'");
            ArrayList<String> parts = newArrayList();
            for(String s : Splitter.on(",").trimResults().split(serialized)) {
                parts.add(s);
            }
            playerId = Long.parseLong(parts.get(0));
            userName = parts.get(1);
        }

        public long getPlayerId() {
            return playerId;
        }

        public String getUserName() {
            return userName;
        }

        @Override
        public String toString() {
            return String.format("%s,%s", playerId, userName);
        }
    }
}
