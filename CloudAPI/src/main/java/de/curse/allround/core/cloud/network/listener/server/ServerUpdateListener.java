package de.curse.allround.core.cloud.network.listener.server;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.listener.SpecificTypePacketListener;
import de.curse.allround.core.cloud.network.packet_types.server.ServerUpdateInfo;
import de.curse.allround.core.cloud.server.Server;
import org.jetbrains.annotations.NotNull;

public class ServerUpdateListener implements SpecificTypePacketListener<ServerUpdateInfo> {
    @Override
    public void listen(@NotNull ServerUpdateInfo serverUpdateInfo) {
        Server server = serverUpdateInfo.getServer();
        if (CloudAPI.getInstance().getServerManager().addServer(server)){
            return;
        }
        Server foundServer = CloudAPI.getInstance().getServerManager().getServer(server.getName()).get();

        foundServer.setHost(server.getHost());
        foundServer.setMaintenance(server.isMaintenance());
        foundServer.setJoinPermissions(server.getJoinPermissions());
        foundServer.setNode(server.getNode());
        foundServer.setNetworkId(server.getNetworkId());
        foundServer.setPort(server.getPort());
        foundServer.setRunning(server.isRunning());
        foundServer.setStatus(server.getStatus());
    }
}
