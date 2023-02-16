package de.curse.allround.core.cloud.network.listener.server;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet_types.server.ServerCreateInfo;
import de.curse.allround.core.cloud.network.packet_types.server.ServerStartRequest;
import de.curse.allround.core.cloud.network.packet_types.server.ServerStartResponse;
import de.curse.allround.core.cloud.server.NodeServer;
import de.curse.allround.core.cloud.servergroup.NodeGroup;

import java.nio.file.Path;

public class ServerListener {
    public void register() {
        NetworkManager.getInstance().getEventBus().listen("SERVER_CREATE_INFO", packet -> {
            ServerCreateInfo serverCreateInfo = packet.convert(ServerCreateInfo.class);
            if (serverCreateInfo.getServer().getNode().equals(CloudAPI.getInstance().getModuleManager().getThisModule().getNetworkId())){
                NodeGroup nodeGroup = (NodeGroup) serverCreateInfo.getServer().getServerGroup();
                nodeGroup.copyTemplate(Path.of("Temp",serverCreateInfo.getServer().getName()));
            }
        });

        NetworkManager.getInstance().getEventBus().listen("SERVER_START_REQUEST",packet -> {
            ServerStartRequest serverStartRequest = packet.convert(ServerStartRequest.class);
            if (!CloudAPI.getInstance().getServerManager().getServer(serverStartRequest.getServer()).get().getNode().equals(CloudAPI.getInstance().getModuleManager().getThisModule().getNetworkId())) return;
            NodeServer nodeServer = (NodeServer) CloudAPI.getInstance().getServerManager().getServer(serverStartRequest.getServer()).get();
            nodeServer.startProcess();
            ServerStartResponse serverStartResponse = new ServerStartResponse(packet.getRequestId(),"SUCCESS");
        });
    }
}
