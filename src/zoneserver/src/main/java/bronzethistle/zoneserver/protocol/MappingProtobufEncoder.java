package bronzethistle.zoneserver.protocol;

import bronzethistle.zoneserver.ClientMessage;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.springframework.stereotype.Component;

/**
 * A protobuf decoder that maps objects to headers.
 *
 * @author elvir.bahtijaragic
 */
@Component
public class MappingProtobufEncoder extends OneToOneEncoder {
    /**
     * @see org.jboss.netty.handler.codec.oneone.OneToOneEncoder#encode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, java.lang.Object)
     */
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        if (!(msg instanceof ClientMessage)) {
            return msg;
        }

        ClientMessage wrapper = (ClientMessage) msg;

        ChannelBuffer header = ChannelBuffers.directBuffer(16);

        header.writeLong(wrapper.getObjectId());
        byte[] data = wrapper.getMessageContent();
        header.writeInt(data.length);

        return ChannelBuffers.wrappedBuffer(header, ChannelBuffers.wrappedBuffer(data));
    }
}