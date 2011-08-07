package bronzethistle.client.gui;

import bronzethistle.messages.client.Message;
import bronzethistle.messages.converters.MessageConverter;
import bronzethistle.messages.protocol.SerializedClientMessage;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

@Component
public class MainForm  {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MainForm.class);

    private JTextField inputText;
    private JPanel mainPanel;
    private JTextPane outputPane;

    @Autowired
    protected Channel channel;

    @Autowired
    protected MessageConverter messageConverter;


    public MainForm() {

        inputText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    logger.debug("sending line " + inputText.getText());
                    // send the line
                    channel.write(new SerializedClientMessage(inputText.getText()));
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

    public void handleClientMessage(SerializedClientMessage rawMessage) {
        Message msg = messageConverter.deserialize(rawMessage);
        outputPane.setText(outputPane.getText() + "\n" + msg.serialize());
    }
}
