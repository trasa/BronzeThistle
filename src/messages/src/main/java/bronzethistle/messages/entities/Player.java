package bronzethistle.messages.entities;

import java.io.Serializable;

public class Player implements Serializable {

    private long playerId;
    private String name;

    public Player() {}

    public Player(long playerId, String name) {
        this.playerId = playerId;
        this.name = name;
    }


    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Player %s - %s",
                playerId, name);
    }
}
