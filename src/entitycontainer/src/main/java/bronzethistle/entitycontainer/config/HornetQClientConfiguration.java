package bronzethistle.entitycontainer.config;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static org.hornetq.core.remoting.impl.netty.TransportConstants.PORT_PROP_NAME;

@Configuration
public class HornetQClientConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(HornetQClientConfiguration.class);

    @Value("${hornetq.port}")
    protected int port;

    @Bean
    public TransportConfiguration transportConfiguration() {
        Map<String, Object> connectionParams = new HashMap<String, Object>();
        connectionParams.put(PORT_PROP_NAME, port);

        return new TransportConfiguration(NettyConnectorFactory.class.getName(), connectionParams);
    }

    @Bean(destroyMethod="close")
    public ClientSessionFactory sessionFactory() throws Exception {
        ServerLocator locator = HornetQClient.createServerLocatorWithoutHA(transportConfiguration());
        return locator.createSessionFactory();
    }

    @Bean(initMethod="start", destroyMethod="close")
    public ClientSession session() throws Exception {
        return sessionFactory().createSession();
    }
}
