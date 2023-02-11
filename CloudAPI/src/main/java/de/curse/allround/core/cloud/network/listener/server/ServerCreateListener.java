package de.curse.allround.core.cloud.network.listener.server;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.listener.SpecificTypePacketListener;
import de.curse.allround.core.cloud.network.packet_types.server.ServerCreateInfo;
import org.jetbrains.annotations.NotNull;

public class ServerCreateListener implements SpecificTypePacketListener<ServerCreateInfo> {
    @Override
    public void listen(@NotNull ServerCreateInfo serverCreateInfo) {
        CloudAPI.getInstance().getServerManager().addServer(serverCreateInfo.getServer());
    }
}
