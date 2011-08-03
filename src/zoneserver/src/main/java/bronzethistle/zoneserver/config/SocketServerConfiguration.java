package bronzethistle.zoneserver.config;

import bronzethistle.zoneserver.handlers.GameMessageHandler;
import bronzethistle.zoneserver.protocol.ChannelMessageHandler;
import bronzethistle.zoneserver.protocol.SimpleStringDecoder;
import bronzethistle.zoneserver.protocol.SimpleStringEncoder;
import com.google.common.base.Strings;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
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

import java.lang.reflect.ParameterizedType;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import static com.google.common.collect.Maps.newHashMap;

@Configuration
public class SocketServerConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SocketServerConfiguration.class);

    @Value("${socket.hostname}")
    protected String hostname;

    @Value("${socket.port}")
    protected int port;

    @Value("${socket.dataWorkerCount}")
    protected int dataWorkerCount;

    @Value("${socket.reuseAddress}")
    protected boolean reuseAddress;

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
            protected ChannelMessageHandler eventHandler = null;

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

                pipeline.addLast("handler", eventHandler);

                return pipeline;
            }
        };
    }

    @Bean(destroyMethod = "releaseExternalResources")
    public ServerBootstrap bootstrap() {
        ServerBootstrap serverBootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool(), dataWorkerCount));

        serverBootstrap.setPipelineFactory(channelPipelineFactory());

        serverBootstrap.setOption("reuseAddress", reuseAddress);

        return serverBootstrap;
    }

    @Bean(destroyMethod = "close")
    public Channel channel() {
        Channel serverChannel = bootstrap().bind(Strings.isNullOrEmpty(hostname) ? new InetSocketAddress(hostname, port) : new InetSocketAddress(port));

        logger.info("Listening on: " + serverChannel.getLocalAddress());

        return serverChannel;
    }

    @Bean
    public Map<String, GameMessageHandler<?>> gameMessageHandlers(List<GameMessageHandler<?>> messageHandlers) {

        Map<String, GameMessageHandler<?>> result = newHashMap();
        for (GameMessageHandler<?> h : messageHandlers) {
            ParameterizedType paramType = (ParameterizedType)h.getClass().getGenericInterfaces()[0];

            Class<?> type = (Class<?>) paramType.getActualTypeArguments()[0];
            result.put(type.getName(), h);
        }
        return result;
    }
}
