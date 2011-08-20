package bronzethistle.zoneserver.handlers.client;


import bronzethistle.messages.client.LoginMessage;
import bronzethistle.messages.client.LoginResponseMessage;
import bronzethistle.zoneserver.Client;
import bronzethistle.zoneserver.Zone;
import bronzethistle.zoneserver.dao.ZoneDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class LoginHandler implements ClientMessageHandler<LoginMessage> {
    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    @Autowired
    protected ZoneDao zoneDao;

    private Zone lobby;

    @PostConstruct
    public void init() {
        lobby = zoneDao.getLobby();
    }

    public void handleMessage(Client client, LoginMessage message) {
        logger.info("login message handled");
        client.setUserName(message.getUserName());

        // put the client in the lobby
        zoneDao.getLobby().addClient(client);

        // send response, telling the client that they are in the lobby
        LoginResponseMessage response = new LoginResponseMessage(client.getPlayerId(), client.getUserName(), lobby.getZoneId());
        client.send(response);
    }
}
