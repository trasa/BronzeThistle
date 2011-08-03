package bronzethistle.zoneserver.handlers;


import bronzethistle.messages.client.LoginMessage;
import bronzethistle.messages.client.SerializedClientMessage;
import bronzethistle.zoneserver.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoginHandler implements GameMessageHandler<LoginMessage> {
    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    public void handleMessage(Client client, LoginMessage message) {
        logger.info("login message handled");

        client.getChannel().write(new SerializedClientMessage("you said hello"));
    }
}
