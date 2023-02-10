package de.curse.allround.core.cloud.network.packet;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import de.curse.allround.core.cloud.CloudAPI;
import org.jetbrains.annotations.NotNull;

public class Publisher {

    private final ConnectionFactory connectionFactory;
    private boolean initialized;

    public Publisher() {
        this.connectionFactory = new ConnectionFactory();
    }

    public void init() throws Exception {
        connectionFactory.setConnectionTimeout(CloudAPI.getInstance().getConfiguration().getRabbitMQConnectionTimeout());
        connectionFactory.setHost(CloudAPI.getInstance().getConfiguration().getRabbitMQHost());
        connectionFactory.setUsername(CloudAPI.getInstance().getConfiguration().getRabbitMQUser());
        connectionFactory.setPassword(CloudAPI.getInstance().getConfiguration().getRabbitMQPassword());
        connectionFactory.setPort(CloudAPI.getInstance().getConfiguration().getRabbitMQPort());

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
