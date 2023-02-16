package de.curse.allround.core.cloud.network.listener.module;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.module.ModuleInfo;
import de.curse.allround.core.cloud.module.NodeInfo;
import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.listener.PacketListener;
import de.curse.allround.core.cloud.network.packet_types.module.ModuleDataResponse;
import org.jetbrains.annotations.NotNull;

public class NodeDataListener implements PacketListener {
    @Override
    public void listen(@NotNull Packet packet) {
        ModuleInfo moduleInfo = new NodeInfo(CloudAPI.getInstance().getModuleManager().getThisModule().getNetworkId(),CloudAPI.getInstance().getModuleManager().getThisModule().getModuleType(),CloudAPI.getInstance().getModuleManager().getThisModule().getName());
        ModuleDataResponse moduleDataResponse = new ModuleDataResponse(packet.getRequestId(), moduleInfo);
        NetworkManager.getInstance().sendPacket(moduleDataResponse);
    }
}
