package de.curse.allround.core.cloud.network.listener;

import de.curse.allround.core.cloud.network.listener.node.NodeMetricsListener;
import de.curse.allround.core.cloud.network.packet.EventBus;
import de.curse.allround.core.cloud.network.packet.NetworkManager;

public class ListenerRegistry {
    public void register(){
        EventBus eventBus = NetworkManager.getInstance().getEventBus();
        eventBus.listenType("MODULE_METRICS_REQUEST",new NodeMetricsListener());
    }
}
