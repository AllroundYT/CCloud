package de.curse.allround.core.cloud.network.listener;

import com.sun.source.tree.IfTree;
import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.module.Module;
import de.curse.allround.core.cloud.network.packet.EventBus;
import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet.PacketConverter;
import de.curse.allround.core.cloud.network.packet_types.module.*;

import java.util.UUID;

public class ModuleListener {
    public void register(){
        EventBus eventBus = CloudAPI.getInstance().getNetworkManager().getEventBus();

        eventBus.listen("MODULE_CONNECT_INFO",packet -> {
            CloudAPI.getInstance().getModuleManager().addModule(packet.convert(ModuleConnectInfo.class).getModule());
        });
        eventBus.listen("MODULE_DISCONNECT_INFO",packet -> {
            CloudAPI.getInstance().getModuleManager().removeModule(packet.convert(ModuleDisconnectInfo.class).getModule().getNetworkId());
        });
        eventBus.listen("MODULE_LIST_REQUEST",packet -> {
            ModuleListResponse moduleListResponse = new ModuleListResponse(
                    packet.getRequestId(),
                    (UUID[]) CloudAPI.getInstance().getModuleManager().getModules().stream().map(Module::getNetworkId).toArray()
            );
            NetworkManager.getInstance().sendPacket(moduleListResponse);
        });
    }
}
