package bronzethistle.zoneserver.protocol;

import bronzethistle.messages.client.SerializedClientMessage;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;


@Component
public class SimpleStringEncoder extends OneToOneEncoder {
    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        if (!(msg instanceof SerializedClientMessage)) {
            return msg;
        }
        SerializedClientMessage wrapper = (SerializedClientMessage) msg;
        return ChannelBuffers.copiedBuffer(wrapper.toString(), Charset.defaultCharset());
    }
}