package de.curse.allround.core.cloud.network.listener;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.module.Module;
import de.curse.allround.core.cloud.network.packet.PacketBus;
import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet_types.module.*;

import java.util.UUID;

public class ModuleListener {
    public void register(){
        PacketBus packetBus = CloudAPI.getInstance().getNetworkManager().getPacketBus();

        packetBus.listen("MODULE_CONNECT_INFO", packet -> {
            CloudAPI.getInstance().getModuleManager().addModule(packet.convert(ModuleConnectInfo.class).getModule());
        });
        packetBus.listen("MODULE_DISCONNECT_INFO", packet -> {
            CloudAPI.getInstance().getModuleManager().removeModule(packet.convert(ModuleDisconnectInfo.class).getModule().getNetworkId());
        });
        packetBus.listen("MODULE_LIST_REQUEST", packet -> {
            ModuleListResponse moduleListResponse = new ModuleListResponse(
                    packet.getRequestId(),
                    (UUID[]) CloudAPI.getInstance().getModuleManager().getModules().stream().map(Module::getNetworkId).toArray()
            );
            NetworkManager.getInstance().sendPacket(moduleListResponse);
        });
    }
}
