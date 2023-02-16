package de.curse.allround.core.cloud.network;

import de.curse.allround.core.cloud.CloudNode;
import de.curse.allround.core.cloud.network.listener.ListenerRegistry;
import de.curse.allround.core.cloud.network.packet.NetworkManager;

import java.nio.file.Path;

public class NodeNetworkManager extends NetworkManager {
    public NodeNetworkManager() {
        super(
                CloudNode.getInstance().getConfiguration().getRabbitMQConnectionTimeout(),
                CloudNode.getInstance().getConfiguration().getRabbitMQHost(),
                CloudNode.getInstance().getConfiguration().getRabbitMQPort(),
                CloudNode.getInstance().getConfiguration().getRabbitMQUser(),
                CloudNode.getInstance().getConfiguration().getRabbitMQPassword()
        );
    }

    @Override
    public void start() {
        super.start();

        new ListenerRegistry().register();
    }
}
