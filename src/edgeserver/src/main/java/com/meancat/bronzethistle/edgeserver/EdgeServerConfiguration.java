package com.meancat.bronzethistle.edgeserver;

import com.meancat.bronzethistle.edgeserver.websocket.WebSocketServerHandler;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBufferFactory;
import org.jboss.netty.buffer.HeapChannelBufferFactory;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.ssl.SslHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.StringUtils;
import org.springframework.util.SystemPropertyUtils;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

@Configuration
@ComponentScan(basePackageClasses = {EdgeServerConfiguration.class})
public class EdgeServerConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(EdgeServerConfiguration.class);

    @Value("${server.externalHostname}")
    public String serverHostname;

    @Value("${netty.bindPort}")
    private int socketBindPort;

    @Bean
    public static PropertyPlaceholderConfigurer propertyConfigurer() {
        PropertyPlaceholderConfigurer bean = new PropertyPlaceholderConfigurer();

        bean.setLocation(new DefaultResourceLoader().getResource(SystemPropertyUtils.resolvePlaceholders("file:${app.home}/conf/config.properties")));
        bean.setSearchSystemEnvironment(true);
        bean.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);

        return bean;
    }

    @Bean
    public Map<String, String> serviceStateProperties() {
        ConcurrentHashMap<String, String> bean = new ConcurrentHashMap<String, String>();
        bean.put("hostname", serverHostname);
        bean.put("port", socketBindPort + "");
        return bean;
    }


    @Bean
    public ChannelFactory socketChannelFactory() {
        return new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
    }

    @Bean
    public ChannelPipelineFactory socketChannelPipelineFactory(final WebSocketServerHandler handler, final WebSocketServerSerDe serDe) {
        ChannelPipelineFactory bean = new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
                ChannelPipeline pipeline = Channels.pipeline();

                pipeline.addLast("decoder", new HttpRequestDecoder());
                pipeline.addLast("aggregator", new HttpChunkAggregator(65536));
                pipeline.addLast("encoder", new HttpResponseEncoder());
                pipeline.addLast("serDe", serDe);
                pipeline.addLast("handler", handler);

                return pipeline;
            }
        };

        return bean;
    }


    @Bean
    public ChannelBufferFactory socketChannelBufferFactory() {
        return new HeapChannelBufferFactory();
    }


    @Bean(destroyMethod="releaseExternalResources")
    public ServerBootstrap socketServerBootstrap(final WebSocketServerHandler handler, final WebSocketServerSerDe serDe) {
        ServerBootstrap bean = new ServerBootstrap(socketChannelFactory());

        bean.setPipelineFactory(socketChannelPipelineFactory(handler, serDe));

        bean.setOption("child.tcpNoDelay", true);
        bean.setOption("child.keepAlive", true);
        bean.setOption("child.bufferFactory", socketChannelBufferFactory());

        InetSocketAddress bindAddress = StringUtils.hasText(serverHostname) ?
                new InetSocketAddress(serverHostname, socketBindPort) :
                new InetSocketAddress(socketBindPort);

        bean.bind(bindAddress);

        logger.info("Socket server listening on [{}]", bindAddress);

        return bean;
    }
}
