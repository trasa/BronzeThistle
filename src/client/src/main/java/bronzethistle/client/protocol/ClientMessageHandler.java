package bronzethistle.client.protocol;

import bronzethistle.client.gui.MainForm;
import bronzethistle.messages.client.Message;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientMessageHandler extends SimpleChannelUpstreamHandler  {
    private static final Logger logger = LoggerFactory.getLogger(ClientMessageHandler.class);

    @Autowired
    protected MainForm mainForm;

      @Override
      public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
          logger.info(e.toString());
          super.handleUpstream(ctx, e);
      }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        logger.info("msg rec'd: " + e.getMessage().toString());
        mainForm.handleClientMessage((Message)e.getMessage());
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
