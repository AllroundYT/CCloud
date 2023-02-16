package de.curse.allround.core.cloud.network.packet;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.Getter;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class Receiver {
    private final ConnectionFactory connectionFactory;
    private final ExecutorService executorService;
    private Connection connection;
    private final int connectionTimeout,port;
    private final String host,user,password;

    public Receiver(int connectionTimeout,String host,int port,String user,String password) {
        this.connectionTimeout = connectionTimeout;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.executorService = Executors.newCachedThreadPool();
        this.connectionFactory = new ConnectionFactory();
    }

    public void init() {
        connectionFactory.setConnectionTimeout(getConnectionTimeout());
        connectionFactory.setHost(getHost());
        connectionFactory.setUsername(getUser());
        connectionFactory.setPassword(getPassword());
        connectionFactory.setPort(getPort());

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
