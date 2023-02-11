package de.curse.allround.core.cloud.network.listener;

import de.curse.allround.core.cloud.network.listener.group.GroupCreateListener;
import de.curse.allround.core.cloud.network.listener.group.GroupDeleteListener;
import de.curse.allround.core.cloud.network.listener.group.GroupUpdateListener;
import de.curse.allround.core.cloud.network.listener.module.ModuleConnectListener;
import de.curse.allround.core.cloud.network.listener.module.ModuleDisconnectListener;
import de.curse.allround.core.cloud.network.listener.player.PlayerConnectListener;
import de.curse.allround.core.cloud.network.listener.player.PlayerDisconnectListener;
import de.curse.allround.core.cloud.network.listener.player.PlayerSwitchServerListener;
import de.curse.allround.core.cloud.network.listener.proxy.ProxyCreateListener;
import de.curse.allround.core.cloud.network.listener.proxy.ProxyDeleteListener;
import de.curse.allround.core.cloud.network.packet.EventBus;
import de.curse.allround.core.cloud.network.packet.NetworkManager;

public class DefaultListener {
    public void register(){
        EventBus eventBus = NetworkManager.getInstance().getEventBus();
        eventBus.listenType("MODULE_CONNECT_INFO",new ModuleConnectListener());
        eventBus.listenType("MODULE_DISCONNECT_INFO",new ModuleDisconnectListener());
        eventBus.listenType("PLAYER_SWITCH_SERVER_INFO",new PlayerSwitchServerListener());
        eventBus.listenType("PLAYER_CONNECT_INFO",new PlayerConnectListener());
        eventBus.listenType("PLAYER_DISCONNECT_INFO",new PlayerDisconnectListener());
        eventBus.listenType("GROUP_CREATE_INFO",new GroupCreateListener());
        eventBus.listenType("GROUP_DELETE_INFO",new GroupDeleteListener());
        eventBus.listenType("GROUP_UPDATE_INFO",new GroupUpdateListener());
        eventBus.listenType("PROXY_CREATE_INFO",new ProxyCreateListener());
        eventBus.listenType("PROXY_DELETE_INFO",new ProxyDeleteListener());
        eventBus.listenType("PROXY_UPDATE_INFO",new ProxyDeleteListener());
        eventBus.listenType("SERVER_CREATE_INFO",new ProxyDeleteListener());
        eventBus.listenType("SERVER_DELETE_INFO",new ProxyDeleteListener());
        eventBus.listenType("SERVER_UPDATE_INFO",new ProxyDeleteListener());

    }
}
