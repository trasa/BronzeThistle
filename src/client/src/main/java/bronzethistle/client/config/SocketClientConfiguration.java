package bronzethistle.client.config;

import bronzethistle.client.protocol.ClientMessageHandler;
import bronzethistle.messages.protocol.SimpleStringDecoder;
import bronzethistle.messages.protocol.SimpleStringEncoder;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.compression.ZlibDecoder;
import org.jboss.netty.handler.codec.compression.ZlibEncoder;
import org.jboss.netty.handler.codec.compression.ZlibWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

@Configuration
public class SocketClientConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SocketClientConfiguration.class);


    @Value("${socket.enableCompression}")
    protected boolean enableCompression;

    @Bean
    public ChannelPipelineFactory channelPipelineFactory() {
        return new ChannelPipelineFactory() {
            @Autowired
            protected ApplicationContext applicationContext = null;

            @Autowired
            protected SimpleStringDecoder mappingDecoder = null;

            @Autowired
            protected SimpleStringEncoder mappingEncoder = null;

            @Autowired
            protected ClientMessageHandler clientMessageHandler = null;

            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();

                // Enable compression if requested
                if (enableCompression) {
                    pipeline.addLast("deflater", new ZlibEncoder(ZlibWrapper.GZIP));
                    pipeline.addLast("inflater", new ZlibDecoder(ZlibWrapper.GZIP));
                }

                // Add the protocol codec first
                pipeline.addLast(applicationContext.getBeanNamesForType(SimpleStringDecoder.class)[0], mappingDecoder);
                pipeline.addLast(applicationContext.getBeanNamesForType(SimpleStringEncoder.class)[0], mappingEncoder);

                pipeline.addLast("handler", clientMessageHandler);

                return pipeline;
            }
        };
    }


    @Bean(destroyMethod = "releaseExternalResources")
    public ClientBootstrap bootstrap() {
        ClientBootstrap clientBootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool(),
                        1));

        clientBootstrap.setPipelineFactory(channelPipelineFactory());

        clientBootstrap.setOption("reuseAddress", false);

        return clientBootstrap;
    }
}