package de.curse.allround.core.cloud.servergroup;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.module.ModuleMetrics;
import de.curse.allround.core.cloud.module.NodeMetrics;
import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet.PacketConverter;
import de.curse.allround.core.cloud.network.packet_types.module.ModuleMetricsRequest;
import de.curse.allround.core.cloud.network.packet_types.module.ModuleMetricsResponse;
import de.curse.allround.core.cloud.server.NodeServer;
import de.curse.allround.core.cloud.server.Server;
import de.curse.allround.core.cloud.server.StartConfiguration;

import java.util.*;

public class NodeGroup extends ServerGroup{
    public NodeGroup(String name, int minServers, int maxServers, Set<String> ignoredStates, StartConfiguration defaultStartConfiguration) {
        super(name, minServers, maxServers, ignoredStates, defaultStartConfiguration);
    }

    @Override
    public Server createServer() {
        int id = CloudAPI.getInstance().getServerManager().getServers().stream().filter(server -> server.getServerGroup().equals(this)).toList().size();
        NodeServer nodeServer = new NodeServer(getName()+"-"+id,this);

        List<ModuleMetrics> nodeMetrics = new ArrayList<>();
        CloudAPI.getInstance().getModuleManager().getNodes().forEach(module -> {
            ModuleMetricsRequest moduleMetricsRequest = new ModuleMetricsRequest(module.getNetworkId());
            NetworkManager.getInstance().sendRequest(moduleMetricsRequest).handle((packet, throwable) -> {
                ModuleMetricsResponse moduleMetricsResponse = PacketConverter.getInstance().convert(packet);
                nodeMetrics.add(moduleMetricsResponse.getModuleMetrics());
                return null;
            });
        });

        Optional<ModuleMetrics> optionalModuleMetrics = nodeMetrics.stream().sorted(Comparator.comparingInt(metrics -> (((NodeMetrics) metrics).getMaxRam()) - ((NodeMetrics) metrics).getRamInUse())).findFirst();
        nodeServer.setNode(optionalModuleMetrics.isEmpty() ? CloudAPI.getInstance().getModuleManager().getMainNode():((NodeMetrics)optionalModuleMetrics.get()).getNetworkId());
        return nodeServer;
    }
}
