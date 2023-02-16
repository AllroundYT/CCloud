package de.curse.allround.core.cloud.network.packet;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import de.curse.allround.core.cloud.CloudAPI;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class Publisher {

    private final ConnectionFactory connectionFactory;
    private boolean initialized;
    private final int connectionTimeout,port;
    private final String host,user,password;
    public Publisher(int connectionTimeout,String host,int port,String user,String password) {
        this.connectionTimeout = connectionTimeout;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.connectionFactory = new ConnectionFactory();
    }

    public void init() throws Exception {
        connectionFactory.setConnectionTimeout(getConnectionTimeout());
        connectionFactory.setHost(getHost());
        connectionFactory.setUsername(getUser());
        connectionFactory.setPassword(getPassword());
        connectionFactory.setPort(getPort());

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare("CLOUD","fanout");
        }
        initialized = true;
    }

    public void publish(@NotNull Packet packet) throws Exception {
        if (!initialized) init();
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.basicPublish("CLOUD", "", null, Packet.serialize(packet));
        }
    }
}
