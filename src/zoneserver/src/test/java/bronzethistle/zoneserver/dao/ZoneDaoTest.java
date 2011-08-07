package bronzethistle.zoneserver.dao;


import bronzethistle.zoneserver.Zone;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ZoneDaoTest {
    private ZoneDao zoneDao;

    @Before
    public void setUp() {
        zoneDao = new ZoneDao();
        zoneDao.init();
    }

    @Test
    public void zerothZoneIsTheLobby() {
        Zone z = zoneDao.getZoneById(0);
        assertEquals(0, z.getZoneId());
        assertEquals("Lobby", z.getName());
    }

}
