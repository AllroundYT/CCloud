package de.curse.allround.core.cloud.network.listener;

import de.curse.allround.core.cloud.network.listener.module.NodeDataListener;
import de.curse.allround.core.cloud.network.listener.server.ServerListener;
import de.curse.allround.core.cloud.network.packet.EventBus;
import de.curse.allround.core.cloud.network.packet.NetworkManager;

public class ListenerRegistry {
    public void register(){
        EventBus eventBus = NetworkManager.getInstance().getEventBus();
        eventBus.listen("MODULE_DATA_REQUEST",new NodeDataListener());

        new ServerListener().register();
    }
}
