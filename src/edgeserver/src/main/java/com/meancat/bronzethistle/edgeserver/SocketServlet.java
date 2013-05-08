package com.meancat.bronzethistle.edgeserver;

import com.meancat.bronzethistle.messages.Message;
import com.meancat.bronzethistle.messages.SerializerDeserializer;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@WebServlet(urlPatterns="/ws",asyncSupported = true) // note that currently this isn't doing anything...
@Component
public class SocketServlet extends WebSocketServlet {
    private static final Logger logger = LoggerFactory.getLogger(SocketServlet.class);

    public final Set<EdgeSocket> sockets = new CopyOnWriteArraySet<EdgeSocket>();

    @Autowired
    protected SerializerDeserializer serde;

    @Autowired
    protected ClientToEdgeMessageHandler clientToEdgeMessageHandler;

    @Override
    public WebSocket doWebSocketConnect(HttpServletRequest httpServletRequest, String protocol) {
        logger.info("connect with {}", protocol);
        return new EdgeSocket();
    }


    class EdgeSocket implements WebSocket.OnTextMessage {
        private Connection connection;

        /**
         * Message from the Client to the EdgeServer
         *
         * @param data the Message encoded in json
         */
        @Override
        public void onMessage(String data) {
            // TODO
            logger.info("received: {}", data);
            try {
                Message incomingMessage = serde.stringToMessage(data);
                // edge sends this message on its way to...?
                clientToEdgeMessageHandler.handle(incomingMessage);

            } catch (IOException e) {
                logger.error("Could not deserialize message from client: " + data, e);
                // TODO send some sort of error message to the client
//                connection.sendMessage("Error!  You gave me a bad message!");
            }
//            try {
//                connection.sendMessage(data);
//            } catch (IOException e) {
//                logger.warn("Failed to send message", e);
//            }

        }

        @Override
        public void onOpen(Connection connection) {
            logger.info("on open");
            sockets.add(this);
            this.connection = connection;

        }

        @Override
        public void onClose(int closeCode, String message) {
            logger.info("on close");
            sockets.remove(this);
        }
    }
}
