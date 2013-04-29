package com.meancat.bronzethistle.edgeserver.websocket;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WebSocketServerHandler extends SimpleChannelUpstreamHandler {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerHandler.class);


    @Override
    public void messageReceived(ChannelHandlerContext context, MessageEvent e) {
        // TODO
//        if (e.getMessage() instanceof Message) {
//            handleMessage((Message)e.getMessage(), e.getChannel());
//        } else {
//            logger.error("Got unexpected message type: {}", e.getMessage());
//        }
    }

//    private void handleMessage(Message message, Channel channel) throws Exception {
//}

    @Override
    public void channelConnected(ChannelHandlerContext context, ChannelStateEvent e) throws Exception {
        super.channelConnected(context, e);
        logger.debug("Channel connected [{}]", e.getChannel());
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext context, ChannelStateEvent e) throws Exception {
        super.channelDisconnected(context, e);
        logger.debug("Channel disconnected [{}]", e.getChannel());
//        try {
//            users.removeUser(e.getChannel(), false);
//        } finally {
//            authListener.removeChannel(e.getChannel());
//        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        String errorUuid = UUID.randomUUID().toString();

        logger.error("Unexpected error - " + errorUuid, e.getCause());

//        sendMessageThenDisconnect(e.getChannel(),
//                new Message(
//                        new MessageHeader(),
//                        new ErrorNotification(ErrorCode.UNEXPECTED_ERROR, "Unexpected server error occurred. (" + errorUuid + ")")));
    }

//    public static void sendMessageThenDisconnect(Channel channel, Message message) {
//        if (channel.isOpen()) {
//            channel.write(message).addListener(ChannelFutureListener.CLOSE);
//        }
//    }
}
