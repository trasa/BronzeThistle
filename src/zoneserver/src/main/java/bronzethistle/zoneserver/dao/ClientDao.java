package bronzethistle.zoneserver.dao;


import bronzethistle.zoneserver.Client;
import org.jboss.netty.channel.Channel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A client data access object.
 *
 * @author elvir.bahtijaragic
 */
@Repository
public class ClientDao implements BeanFactoryAware {
    private AtomicLong playerIdCounter = new AtomicLong(0L);

    private ConcurrentHashMap<Integer, Client> clients = new ConcurrentHashMap<Integer, Client>();
    private ConcurrentHashMap<Long, Client> players = new ConcurrentHashMap<Long, Client>();

    private DefaultListableBeanFactory beanFactory = null;

    /**
     * Gets a client by a channel.
     *
     * @param channel
     * @return
     */
    public Client getClientByChannel(Channel channel) {
        return clients.get(channel.getId());
    }

    /**
     * Gets a client by a player id.
     *
     * @param playerId
     * @return
     */
    public Client getClientByPlayerId(long playerId) {
        return players.get(playerId);
    }

    /**
     * Adds a client attached to the given channel.
     *
     * @param channel
     * @return
     */
    public Client addClientFromChannel(Channel channel) {
        Client client = new Client(playerIdCounter.incrementAndGet(), channel);

        beanFactory.autowireBean(client);

        beanFactory.applyBeanPostProcessorsBeforeInitialization(client, null);
        client.initialize();
        beanFactory.applyBeanPostProcessorsAfterInitialization(client, null);

        clients.put(channel.getId(), client);
        players.put(client.getPlayerId(), client);

        return client;
    }

    /**
     * Removes a client by a channel.
     *
     * @param channel
     * @return
     */
    public Client removeClientByChannel(Channel channel) {
        Client client = clients.remove(channel.getId());

        if (client != null) {
            players.remove(client.getPlayerId());

            client.close();
        }

        return client;
    }

    /**
     * Removes a client by a player id.
     *
     * @param playerId
     * @return
     */
    public Client removeClientByPlayerId(long playerId) {
        Client client = players.remove(playerId);

        if (client != null) {
            clients.remove(client.getChannelId());

            client.close();
        }

        return client;
    }

    /**
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }
}
