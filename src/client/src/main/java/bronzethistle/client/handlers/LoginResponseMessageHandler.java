package bronzethistle.client.handlers;

import bronzethistle.client.gui.MainForm;
import bronzethistle.messages.client.LoginResponseMessage;
import bronzethistle.messages.client.RegisterObjectMessage;
import bronzethistle.messages.entities.Player;
import org.jboss.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginResponseMessageHandler implements ClientMessageHandler<LoginResponseMessage> {

    @Autowired
    protected Channel channel;

    public void handleMessage(LoginResponseMessage msg) {
//        Player p = new Player();
//        p.setPlayerId(msg.getPlayerId());
//        p.setName(msg.getUserName());
//        p.setCurrentZoneId(msg.getZoneId());
//        mainForm.setPlayer(p);

        // client needs to know about the player object representing them.
        RegisterObjectMessage request = new RegisterObjectMessage();
        request.setEntityId("player." + msg.getPlayerId());
        channel.write(request);

        request = new RegisterObjectMessage();
        request.setEntityId("zone." + msg.getZoneId());
        channel.write(request);

        // how does client find out who else is in the room?
        // how does everyone else find out about this client?
    }
}
