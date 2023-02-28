package de.curse.allround.core.cloud.network.listener;

import de.curse.allround.core.cloud.network.listener.group.GroupListener;
import de.curse.allround.core.cloud.network.listener.module.NodeDataListener;
import de.curse.allround.core.cloud.network.listener.server.ServerListener;
import de.curse.allround.core.cloud.network.packet.PacketBus;
import de.curse.allround.core.cloud.network.packet.NetworkManager;

public class ListenerRegistry {
    public void register(){
        PacketBus packetBus = NetworkManager.getInstance().getPacketBus();
        packetBus.listen("MODULE_DATA_REQUEST",new NodeDataListener());

        new ServerListener().register();
        new ProxyListener().register();
        new GroupListener().register();
    }
}
