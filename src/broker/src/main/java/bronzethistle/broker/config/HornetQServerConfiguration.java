package bronzethistle.broker.config;

import bronzethistle.broker.ExampleMessageInterceptor;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.api.core.management.HornetQServerControl;
import org.hornetq.core.config.impl.ConfigurationImpl;
import org.hornetq.core.management.impl.HornetQServerControlImpl;
import org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.core.server.HornetQServers;
import org.hornetq.core.server.JournalType;
import org.hornetq.core.server.management.ManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.hornetq.core.remoting.impl.netty.TransportConstants.PORT_PROP_NAME;

@Configuration
public class HornetQServerConfiguration {
    private static final Logger log = LoggerFactory.getLogger(HornetQServerConfiguration.class);

    @Value("${hornetq.port}")
    protected int port;

    @Bean
    public org.hornetq.core.config.Configuration configuration() {
        org.hornetq.core.config.Configuration configuration = new ConfigurationImpl();
        configuration.setPersistenceEnabled(true);
        configuration.setSecurityEnabled(false);
        configuration.setJournalType(JournalType.NIO);

        List<String> interceptors = newArrayList();
        interceptors.add(ExampleMessageInterceptor.class.getName());
        configuration.setInterceptorClassNames(interceptors);

        Map<String, Object> connectionParams = new HashMap<String, Object>();
        connectionParams.put("port", port);
        configuration.getAcceptorConfigurations().add(new TransportConfiguration(NettyAcceptorFactory.class.getName(), connectionParams));

        return configuration;
    }

    @Bean
    public HornetQServer hornetqserver() throws Exception {
        HornetQServer server = HornetQServers.newHornetQServer(this.configuration());
        server.start();
        return server;
    }

    @Bean HornetQServerControlImpl hornetqserverimpl() throws Exception {
        return hornetqserver().getHornetQServerControl();
    }

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
        ClientSession session = sessionFactory().createSession();
        session.start();
        return session;
    }
}
