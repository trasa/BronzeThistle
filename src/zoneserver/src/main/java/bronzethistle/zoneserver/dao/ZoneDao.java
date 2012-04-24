package bronzethistle.zoneserver.dao;

import bronzethistle.zoneserver.Zone;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import static com.google.common.collect.Maps.newConcurrentMap;

@Repository
public class ZoneDao  {
    private AtomicLong zoneIdCounter = new AtomicLong(0L);

    private ConcurrentMap<Long, Zone> zones;

    public static final Long LOBBY_ZONE_ID = 0L;

    public Zone getLobby() {
        return zones.get(LOBBY_ZONE_ID);
    }

    @PostConstruct
    public void init() {
        zones = newConcurrentMap();
        Zone lobby = createZone();
        lobby.setName("Lobby");
    }

    private synchronized Zone createZone(long zoneId) {
        Zone newZone = new Zone(zoneId);
        zones.put(zoneId, newZone);
        return newZone;
    }

    private synchronized Zone createZone() {
        long zoneId = zoneIdCounter.getAndIncrement();
        return createZone(zoneId);
    }

    public Zone getZoneById(long zoneId) {
        return zones.get(zoneId);
    }
}
