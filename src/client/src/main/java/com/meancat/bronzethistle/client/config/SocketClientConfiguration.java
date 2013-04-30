package com.meancat.bronzethistle.client.config;

import com.meancat.bronzethistle.client.protocol.ClientChannelHandler;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

@Configuration
public class SocketClientConfiguration {
    private static final Logger log = LoggerFactory.getLogger(SocketClientConfiguration.class);


    @Value("${socket.enableCompression}")
    protected boolean enableCompression;

    @Bean
    public ChannelPipelineFactory channelPipelineFactory() {
        return new ChannelPipelineFactory() {
            @Autowired
            protected ApplicationContext applicationContext = null;

            @Autowired
            protected ClientChannelHandler clientMessageHandler = null;

            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();

                // using java serializer for now.
                pipeline.addLast("decoder", new ObjectDecoder());
                pipeline.addLast("encoder", new ObjectEncoder());

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

    @Bean(destroyMethod = "close")
    public Channel channel() throws Exception {
        ClientBootstrap boot = bootstrap();
        // Start the connection attempt.
        // TODO pull address and such from configuration...
        ChannelFuture future = boot.connect(new InetSocketAddress("localhost", 8114));
        // Wait until the connection attempt succeeds or fails.
        org.jboss.netty.channel.Channel channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {
            boot.releaseExternalResources();
            throw new Exception("Failed to connect to zone server.", future.getCause());
        }
        return channel;
    }
}
