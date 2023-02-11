package de.curse.allround.core.cloud.network;

import de.curse.allround.core.cloud.network.listener.ListenerRegistry;
import de.curse.allround.core.cloud.network.packet.NetworkManager;

public class NodeNetworkManager extends NetworkManager {
    public NodeNetworkManager() {
        super();
    }

    @Override
    public void start() {
        super.start();

        new ListenerRegistry().register();
    }
}
