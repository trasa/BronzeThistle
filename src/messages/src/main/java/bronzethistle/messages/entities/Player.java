package bronzethistle.messages.entities;

public class Player {

    private long playerId;
    private String name;
    private long currentZoneId;


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

    public long getCurrentZoneId() {
        return currentZoneId;
    }

    public void setCurrentZoneId(long currentZoneId) {
        this.currentZoneId = currentZoneId;
    }
}
