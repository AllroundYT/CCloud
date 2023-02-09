package de.curse.allround.core.cloud.network.packet;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.jetbrains.annotations.NotNull;

public class Publisher {

    private final ConnectionFactory connectionFactory;
    private boolean initialized;

    public Publisher() {
        this.connectionFactory = new ConnectionFactory();
        connectionFactory.setConnectionTimeout(10000);
        connectionFactory.setHost("localhost");
    }

    public void init() throws Exception {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(PacketChannel.CORE.name(), false, false, false, null);
            channel.queueDeclare(PacketChannel.CLOUD.name(), false, false, false, null);
        }
        initialized = true;
    }

    public void publish(@NotNull Packet packet) throws Exception {
        if (!initialized) init();
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.basicPublish("", packet.getChannel().name(), null, Packet.serialize(packet));
        }
    }
}
