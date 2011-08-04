package bronzethistle.messages.converters;

import bronzethistle.messages.client.LoginMessage;
import bronzethistle.messages.client.Message;
import bronzethistle.messages.protocol.SerializedClientMessage;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

public class MessageConverterTest {

    private MessageConverter converter;

    @Before
    public void setUp() {
        converter = new MessageConverter();
    }

    @Test
    public void loginMessage() {
        SerializedClientMessage rawMsg = new SerializedClientMessage("login|myusername");
        Message msg = converter.deserialize(rawMsg);
        assertSame(LoginMessage.class, msg.getClass());
        LoginMessage loginMsg = (LoginMessage)msg;
        assertEquals("myusername", loginMsg.getUserName());
    }
}
