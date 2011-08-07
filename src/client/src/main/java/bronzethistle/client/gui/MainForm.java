package bronzethistle.client.gui;

import bronzethistle.messages.client.LoginMessage;
import bronzethistle.messages.client.Message;
import org.jboss.netty.channel.Channel;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@Component
public class MainForm  {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MainForm.class);

    private JTextField inputText;
    private JPanel mainPanel;
    private JTextPane outputPane;

    @Autowired
    protected Channel channel;

    public MainForm() {

        inputText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    // send the line
                    // TODO so...this needs to be all redone.
//                    logger.info("sending line " + inputText.getText());
                    // or need a way to turn a line of text --> Message (instead of the old string buffer messages)
                    channel.write(new LoginMessage("username")); // TODO

                    inputText.setText("");
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
        outputPane.setText(outputPane.getText() + "\n" + msg.serialize());
    }
}
