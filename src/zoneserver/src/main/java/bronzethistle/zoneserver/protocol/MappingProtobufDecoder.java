package bronzethistle.zoneserver.protocol;

import bronzethistle.zoneserver.ClientMessage;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.springframework.stereotype.Component;


/**
 * A protobuf decoder that maps headers to objects.
 *
 * @author elvir.bahtijaragic
 */
@Component
public class MappingProtobufDecoder extends OneToOneDecoder {

    protected Object decode(ChannelHandlerContext ctx, org.jboss.netty.channel.Channel channel, Object msg) throws Exception {
        if (!(msg instanceof ChannelBuffer)) {
            return msg;
        }
        ChannelBuffer buf = (ChannelBuffer) msg;
        int length = buf.readInt();
        byte[] data = new byte[length];
        buf.readBytes(data);

        return new ClientMessage(data);
    }
}
