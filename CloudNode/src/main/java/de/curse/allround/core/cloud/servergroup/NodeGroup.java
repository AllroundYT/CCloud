package de.curse.allround.core.cloud.servergroup;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.module.ModuleInfo;
import de.curse.allround.core.cloud.module.NodeInfo;
import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet.PacketConverter;
import de.curse.allround.core.cloud.network.packet_types.module.ModuleDataRequest;
import de.curse.allround.core.cloud.network.packet_types.module.ModuleDataResponse;
import de.curse.allround.core.cloud.network.packet_types.server.ServerCreateInfo;
import de.curse.allround.core.cloud.server.NodeServer;
import de.curse.allround.core.cloud.server.Server;
import de.curse.allround.core.cloud.server.StartConfiguration;

import java.nio.file.Path;
import java.util.*;

public class NodeGroup extends ServerGroup{
    public NodeGroup(String name, int minServers, int maxServers, Set<String> ignoredStates, StartConfiguration defaultStartConfiguration) {
        super(name, minServers, maxServers, ignoredStates, defaultStartConfiguration);
    }

    public void copyTemplate(Path path){

    }

    @Override
    public Server createServer() {
        int id = CloudAPI.getInstance().getServerManager().getServers().stream().filter(server -> server.getServerGroup().equals(this)).toList().size();
        NodeServer nodeServer = new NodeServer(getName()+"-"+id,this);

        List<ModuleInfo> nodeMetrics = new ArrayList<>();
        CloudAPI.getInstance().getModuleManager().getNodes().forEach(module -> {
            ModuleDataRequest moduleDataRequest = new ModuleDataRequest(module.getNetworkId());
            NetworkManager.getInstance().sendRequest(moduleDataRequest).handle((packet, throwable) -> {
                ModuleDataResponse moduleDataResponse = PacketConverter.getInstance().convert(packet);
                nodeMetrics.add(moduleDataResponse.getModuleInfo());
                return null;
            });
        });

        Optional<ModuleInfo> optionalModuleMetrics = nodeMetrics.stream().sorted(Comparator.comparingInt(metrics -> (((NodeInfo) metrics).getMaxRam()) - ((NodeInfo) metrics).getRamInUse())).findFirst();
        nodeServer.setNode(optionalModuleMetrics.isEmpty() ? CloudAPI.getInstance().getModuleManager().getMainNode():((NodeInfo)optionalModuleMetrics.get()).getNetworkId());

        if (nodeServer.getNode().equals(CloudAPI.getInstance().getModuleManager().getThisModule().getNetworkId())){
            //

        }

        ServerCreateInfo serverCreateInfo = new ServerCreateInfo(nodeServer);
        NetworkManager.getInstance().sendPacket(serverCreateInfo);
        return nodeServer;
    }
}
