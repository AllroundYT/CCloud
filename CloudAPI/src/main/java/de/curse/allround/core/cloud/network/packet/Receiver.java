package de.curse.allround.core.cloud.network.packet;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Receiver {
    private final ConnectionFactory connectionFactory;
    private final ExecutorService executorService;
    private Connection connection;

    public Receiver() {
        this.executorService = Executors.newCachedThreadPool();
        this.connectionFactory = new ConnectionFactory();
        connectionFactory.setConnectionTimeout(10000);
        connectionFactory.setHost("localhost");
    }

    public void init() throws Exception {
        connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(PacketChannel.CORE.name(), false, false, false, null);
        channel.queueDeclare(PacketChannel.CLOUD.name(), false, false, false, null);

        executorService.submit(() -> {
            while (connection.isOpen()) {
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    NetworkManager.getInstance().getEventBus().onPacket(Packet.deserialize(delivery.getBody()));
                };

                try {
                    channel.basicConsume(PacketChannel.CORE.name(), true, deliverCallback, s -> {
                    });
                    channel.basicConsume(PacketChannel.CLOUD.name(), true, deliverCallback, s -> {
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void stop() throws IOException {
        connection.close();
    }
}
