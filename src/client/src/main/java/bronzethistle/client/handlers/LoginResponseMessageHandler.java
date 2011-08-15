package bronzethistle.client.handlers;

import bronzethistle.messages.client.LoginResponseMessage;
import bronzethistle.messages.client.RequestEntityMessage;
import org.jboss.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginResponseMessageHandler implements ClientMessageHandler<LoginResponseMessage> {

    @Autowired
    protected Channel channel;

    public void handleMessage(LoginResponseMessage msg) {
        // client needs to know about the player object representing them.
        RequestEntityMessage request = new RequestEntityMessage();
        request.setEntityId("player." + msg.getPlayerId());
        channel.write(request);

        request = new RequestEntityMessage();
        request.setEntityId("zone." + msg.getZoneId());
        channel.write(request);

        // how does client find out who else is in the room?
        // how does everyone else find out about this client?
    }
}
