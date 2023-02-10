package de.curse.allround.core.cloud.network.packet;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.Objects;
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

    public void init() {
        executorService.submit(() -> {
            try {
                connection = connectionFactory.newConnection();
                Channel channel = connection.createChannel();
                channel.exchangeDeclare("CLOUD", "fanout");

                String queue = channel.queueDeclare().getQueue();
                channel.queueBind(queue, "CLOUD", "");
                while (connection.isOpen()) {
                    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                        NetworkManager.getInstance().getEventBus().onPacket(Objects.requireNonNull(Packet.deserialize(delivery.getBody())));
                    };

                    try {
                        channel.basicConsume(queue, true, deliverCallback, s -> {
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public void stop() throws IOException {
        connection.close();
    }
}
