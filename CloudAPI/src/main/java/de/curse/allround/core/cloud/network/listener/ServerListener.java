package de.curse.allround.core.cloud.network.listener;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.PacketBus;
import de.curse.allround.core.cloud.network.packet_types.server.ServerCreateInfo;
import de.curse.allround.core.cloud.network.packet_types.server.ServerDeleteInfo;
import de.curse.allround.core.cloud.network.packet_types.server.ServerUpdateInfo;
import de.curse.allround.core.cloud.server.Server;

public class ServerListener {
    public void register(){
        PacketBus packetBus = CloudAPI.getInstance().getNetworkManager().getPacketBus();

        packetBus.listen("SERVER_CREATE_INFO", packet -> {
            CloudAPI.getInstance().getServerManager().addServer(packet.convert(ServerCreateInfo.class).getServer());
        });
        packetBus.listen("SERVER_DELETE_INFO", packet -> {
            CloudAPI.getInstance().getServerManager().removeServer(packet.convert(ServerDeleteInfo.class).getServer());
        });
        packetBus.listen("SERVER_UPDATE_INFO", packet -> {
            Server server = packet.convert(ServerUpdateInfo.class).getServer();
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
        });

        packetBus.listen("SERVER_DATA_REQUEST", packet -> {

        });

        packetBus.listen("SERVER_LIST_REQUEST", packet -> {

        });
    }
}
