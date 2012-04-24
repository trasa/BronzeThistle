package bronzethistle.zoneserver.dao;


import bronzethistle.zoneserver.Zone;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ZoneDaoTest {
    private ZoneDao zoneDao;

    @Before
    public void setUp() {
        zoneDao = new ZoneDao();
        zoneDao.init();
    }

    @Test
    public void zerothZoneIsTheLobby() {
        Zone z = zoneDao.getZoneById(ZoneDao.LOBBY_ZONE_ID);
        assertEquals(ZoneDao.LOBBY_ZONE_ID, z.getZoneId());
        assertEquals("Lobby", z.getName());
    }

    @Test
    @Ignore("not implemented yet")
    public void create_a_zone() {
        // needs to not overwrite the existing 0th zone,
        // start numbering from 1, etc.
        assertTrue("todo", false);
    }
}
