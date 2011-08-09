package bronzethistle.broker.config;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.config.impl.ConfigurationImpl;
import org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.core.server.HornetQServers;
import org.hornetq.core.server.JournalType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class HornetQConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(HornetQConfiguration.class);

    @Value("${hornetq.port}")
    protected int port;

    @Bean
    public org.hornetq.core.config.Configuration configuration() {
        org.hornetq.core.config.Configuration configuration = new ConfigurationImpl();
        configuration.setPersistenceEnabled(true);
        configuration.setSecurityEnabled(false);
        configuration.setJournalType(JournalType.NIO);

        Map<String, Object> connectionParams = new HashMap<String, Object>();
        connectionParams.put("port", port);
        configuration.getAcceptorConfigurations().add(new TransportConfiguration(NettyAcceptorFactory.class.getName(), connectionParams));

        return configuration;
    }

    @Bean
    public HornetQServer hornetqserver() throws Exception {
        HornetQServer server = HornetQServers.newHornetQServer(configuration());
        server.start();
        return server;
    }
}
