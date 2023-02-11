package de.curse.allround.core.cloud.network.listener.server;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.listener.SpecificTypePacketListener;
import de.curse.allround.core.cloud.network.packet_types.server.ServerDeleteInfo;
import org.jetbrains.annotations.NotNull;

public class ServerDeleteListener implements SpecificTypePacketListener<ServerDeleteInfo> {
    @Override
    public void listen(@NotNull ServerDeleteInfo packetType) {
        CloudAPI.getInstance().getServerManager().removeServer(packetType.getServer());
    }
}
