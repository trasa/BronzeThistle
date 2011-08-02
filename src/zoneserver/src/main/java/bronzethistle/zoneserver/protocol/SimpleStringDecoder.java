package bronzethistle.zoneserver.protocol;

import bronzethistle.zoneserver.ClientMessage;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;


@Component
public class SimpleStringDecoder extends OneToOneDecoder {

    @Override
    protected Object decode(ChannelHandlerContext ctx, org.jboss.netty.channel.Channel channel, Object msg) throws Exception {
        if (!(msg instanceof ChannelBuffer)) {
            return msg;
        }
        ChannelBuffer buf = (ChannelBuffer) msg;
        String rawData = buf.toString(Charset.defaultCharset()).trim();
        return new ClientMessage(rawData);
    }
}
