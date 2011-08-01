package bronzethistle.zoneserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Zone {
    private static final Logger logger = LoggerFactory.getLogger(Zone.class);

    private int zoneId;

    public Zone(int zoneId) {
        this.zoneId = zoneId;
    }


//    public void onEventConnect(ConnectMessage msg) {
//        logger.info("Player connected: user " + msg.getUserName());
//    }
}
