package bronzethistle.zoneserver.protocol;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Handles channel events and bus events.
 *
 * @author Elvir
 */
public class ChannelMessageHandler extends SimpleChannelUpstreamHandler {
    private static final Logger logger = LoggerFactory.getLogger(ChannelMessageHandler.class);

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        logger.info("msg rec'd");
//        Client client = clientDao.getClientByChannel(e.getChannel());
//        client.handleClientMessage((ClientMessage) e.getMessage());
    }

    /**
     * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#exceptionCaught(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ExceptionEvent)
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        logger.error("Unexpected exception from: " + e.getChannel(), e.getCause());
    }

    /**
     * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#channelConnected(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
     */
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        logger.info("channel connected");
//        clientDao.addClientFromChannel(e.getChannel());
    }

    /**
     * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#channelDisconnected(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
     */
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        logger.info("channel disconnected");
//        clientDao.removeClientByChannel(e.getChannel());
    }
}
