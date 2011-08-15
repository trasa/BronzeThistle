package bronzethistle.zoneserver.handlers;


import bronzethistle.messages.client.LoginMessage;
import bronzethistle.messages.client.LoginResponseMessage;
import bronzethistle.zoneserver.Client;
import bronzethistle.zoneserver.Zone;
import bronzethistle.zoneserver.dao.ZoneDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginHandler implements GameMessageHandler<LoginMessage> {
    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    @Autowired
    protected ZoneDao zoneDao;

    public void handleMessage(Client client, LoginMessage message) {
        logger.info("login message handled");
        client.setUserName(message.getUserName());

        // on login assign the player into the lobby.
        Zone lobby = zoneDao.getLobby();

        // send response, telling the client that they should go to the lobby.
        LoginResponseMessage response = new LoginResponseMessage(client.getPlayerId(), client.getUserName(), lobby.getZoneId());
        client.send(response);

//        lobby.addClient(client);
    }
}
