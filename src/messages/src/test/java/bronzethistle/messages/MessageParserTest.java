package bronzethistle.messages;

import bronzethistle.messages.client.LoginMessage;
import bronzethistle.messages.client.LoginResponseMessage;
import bronzethistle.messages.client.Message;
import bronzethistle.messages.client.MessageType;
import bronzethistle.messages.client.ZoneClientMessage;
import com.google.common.base.Joiner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class MessageParserTest {

    @Test
    public void loginMessage() throws MessageParserException {
        String s = Joiner.on(" ").join(MessageType.LOGIN.getCode(), "username");
        MessageParser p = new MessageParser(s);
        Message m = p.parse();

        assertEquals(LoginMessage.class, m.getClass());
        assertEquals("username", ((LoginMessage)m).getUserName());
    }

    @Test
    public void loginResponseMessage() throws MessageParserException {
        String s = Joiner.on(" ").join(MessageType.LOGIN_RESPONSE.getCode(), 100, "username");
        MessageParser p = new MessageParser(s);
        Message m = p.parse();

        assertEquals(LoginResponseMessage.class, m.getClass());
        LoginResponseMessage rm = (LoginResponseMessage)m;
        assertEquals("username", rm.getUserName());
        assertEquals(100, rm.getPlayerId());
    }

    @Test
    public void zoneClientMessage() throws MessageParserException {
        // TODO
        String s = Joiner.on(" ").join(MessageType.ZONE_CLIENT.getCode(), 100, "username");
        MessageParser p = new MessageParser(s);
        Message m = p.parse();

        assertEquals(ZoneClientMessage.class, m.getClass());
        ZoneClientMessage zm = (ZoneClientMessage)m;

        // TODO
    }
}
