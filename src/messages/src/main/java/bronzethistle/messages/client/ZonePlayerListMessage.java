package bronzethistle.messages.client;

import bronzethistle.messages.entities.Player;

import java.util.List;

public class ZonePlayerListMessage implements Message {
    private List<Player> players;

    public ZonePlayerListMessage(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public MessageType getMessageType() {
        return MessageType.ZONE_PLAYER_LIST;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ZonePlayerListMessage: ");
        for(Player p: players){
            sb.append(p.toString()).append(" ");
        }
        return sb.toString();
    }
}
