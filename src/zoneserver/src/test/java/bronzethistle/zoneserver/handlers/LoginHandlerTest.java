package bronzethistle.zoneserver.handlers;

import bronzethistle.messages.client.LoginMessage;
import bronzethistle.messages.client.Message;
import bronzethistle.zoneserver.Client;
import bronzethistle.zoneserver.Zone;
import bronzethistle.zoneserver.dao.ZoneDao;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.jboss.netty.channel.Channel;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class LoginHandlerTest {
    private LoginHandler loginHandler;
    private Channel mockChannel;
    private Zone lobby;

    @Before
    public void setUp() {
        loginHandler = new LoginHandler();
        mockChannel = mock(Channel.class);
        ZoneDao mockZoneDao = mock(ZoneDao.class);

        loginHandler.zoneDao = mockZoneDao;
        lobby = new Zone(0L);
        when(mockZoneDao.getLobby()).thenReturn(lobby);
    }

    @Test
    @Ignore("what happens on login is undergoing a bunch of changes")
    public void onLoginSendResponse() {
        Client client = new Client(1, mockChannel);
        loginHandler.handleMessage(client, new LoginMessage("username"));
        // assert that channel.send happened
        verify(mockChannel, times(2))
                .write(any(Message.class));
    }


    @Test
    @Ignore("what happens on login is undergoing a bunch of changes")
    public void onLoginAddClientToLobby() {
        Client client = new Client(1, mockChannel);
        loginHandler.handleMessage(client, new LoginMessage("username"));

        // assert that the client is in the lobby zone now
        assertNotNull(lobby.getClientById(1L));
    }
}
