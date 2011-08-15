package bronzethistle.messages.client;

import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ZoneClientMessage implements Message {
    private static final Logger log = LoggerFactory.getLogger(ZoneClientMessage.class);

    private List<ZoneClientRecord> clients = newArrayList();

    public ZoneClientMessage() {
    }

    public void addClient(ZoneClientRecord client) {
        clients.add(client);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ZoneClient: ");
        for (ZoneClientRecord r : clients) {
            sb.append("(").append(r.toString()).append(") ");
        }
        return sb.toString();
    }

    public static class ZoneClientRecord implements Serializable {
        private final long playerId;
        private final String userName;

        public ZoneClientRecord(long playerId, String userName) {

            this.playerId = playerId;
            this.userName = userName;
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
