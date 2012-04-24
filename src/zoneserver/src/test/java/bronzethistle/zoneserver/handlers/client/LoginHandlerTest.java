package bronzethistle.zoneserver.handlers.client;

import bronzethistle.messages.client.LoginMessage;
import bronzethistle.messages.client.LoginResponseMessage;
import bronzethistle.messages.client.Message;
import bronzethistle.messages.client.ZonePlayerListMessage;
import bronzethistle.messages.entities.Player;
import bronzethistle.zoneserver.Client;
import bronzethistle.zoneserver.Zone;
import bronzethistle.zoneserver.dao.ZoneDao;
import bronzethistle.zoneserver.handlers.client.LoginHandler;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.jboss.netty.channel.Channel;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginHandlerTest {

    private LoginHandler loginHandler;
    private Zone lobby;

    @Mock
    private Channel mockChannel;
    @Mock
    private ZoneDao mockZoneDao;


    @Before
    public void setUp() {
        loginHandler = new LoginHandler();
        loginHandler.zoneDao = mockZoneDao;
        lobby = new Zone(0L);
        when(mockZoneDao.getLobby()).thenReturn(lobby);

        loginHandler.init();
    }

    @Test
    public void onLogin_SendLoginResponse() {
        Client client = new Client(1, mockChannel);

        loginHandler.handleMessage(client, new LoginMessage("username"));

        // assert that channel.send happened:
        // first message is LoginResponse
        // 2nd message is ZonePlayerListMessage
        ArgumentCaptor argument = ArgumentCaptor.forClass(Message.class);
        verify(mockChannel, times(2)).write(argument.capture());

        List args = argument.getAllValues();

        LoginResponseMessage loginResponseMessage = (LoginResponseMessage)args.get(0);
        assertEquals(1, loginResponseMessage.getPlayerId());
        assertEquals("username", loginResponseMessage.getUserName());
        assertEquals(ZoneDao.LOBBY_ZONE_ID, loginResponseMessage.getZoneId()); // lobby

        ZonePlayerListMessage zonePlayerListMessage = (ZonePlayerListMessage)args.get(1);
        assertEquals(1, zonePlayerListMessage.getPlayers().size());
        Player p = zonePlayerListMessage.getPlayers().get(0);
        assertEquals(1, p.getPlayerId());
        assertEquals("username", p.getName());
    }


    @Test
    public void onLoginAddClientToLobby() {
        Client client = new Client(1, mockChannel);
        loginHandler.handleMessage(client, new LoginMessage("username"));

        // assert that the client is in the lobby zone now
        assertNotNull(lobby.getClientById(1L));
    }
}
