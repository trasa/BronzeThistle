package bronzethistle.client.gui;

import bronzethistle.client.protocol.ClientChannelHandler;
import bronzethistle.messages.MessageParser;
import bronzethistle.messages.MessageParserException;
import bronzethistle.messages.client.Message;
import bronzethistle.messages.entities.Player;
import org.jboss.netty.channel.Channel;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// DependsOn is to avoid a circular dependency on Channel (via ClientChannelHandler and LoginResponseMessageHandler)
@Component
@DependsOn("clientChannelHandler")
public class MainForm  {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MainForm.class);

    private JTextField inputText;
    private JPanel mainPanel;
    private JTextPane outputPane;

    private Player player;

    public Player getPlayer() { return player; }
    public void setPlayer(Player p) { player = p; }


    @Autowired
    protected Channel channel;

    public MainForm() {

        inputText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    try {
                        Message msg = new MessageParser(inputText.getText()).parse();
                        channel.write(msg);
                        inputText.setText("");
                    } catch (MessageParserException ex) {
                        inputText.setText(ex.toString());
                    }
                    return;
                }
                super.keyTyped(e);
            }
        });
    }

    public Container getContentPane() {
        return mainPanel;
    }

    public void handleClientMessage(Message msg) {
        outputPane.setText(outputPane.getText() + "\n" + msg.toString());
    }
}
