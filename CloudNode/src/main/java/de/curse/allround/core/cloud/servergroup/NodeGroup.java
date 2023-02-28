package de.curse.allround.core.cloud.servergroup;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.CloudNode;
import de.curse.allround.core.cloud.module.ModuleInfo;
import de.curse.allround.core.cloud.module.NodeInfo;
import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet.PacketConverter;
import de.curse.allround.core.cloud.network.packet_types.module.ModuleDataRequest;
import de.curse.allround.core.cloud.network.packet_types.module.ModuleDataResponse;
import de.curse.allround.core.cloud.network.packet_types.server.ServerCreateInfo;
import de.curse.allround.core.cloud.network.packet_types.group.GroupTemplateRequest;
import de.curse.allround.core.cloud.network.packet_types.group.GroupTemplateResponse;
import de.curse.allround.core.cloud.server.NodeServer;
import de.curse.allround.core.cloud.server.Server;
import de.curse.allround.core.cloud.server.StartConfiguration;
import de.curse.allround.core.cloud.util.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class NodeGroup extends ServerGroup{
    public NodeGroup(String name, int minServers, int maxServers, Set<String> ignoredStates, StartConfiguration defaultStartConfiguration) {
        super(name, minServers, maxServers, ignoredStates, defaultStartConfiguration);
    }

    public void copyTemplate(Path path){
        if (CloudNode.getInstance().getServerGroupManager().getUpdatedTemplates().contains(getName())){
            updateTemplate().thenRun(() -> {
                try {
                    FileUtils.copy(Path.of("Storage","Templates","Servers",getName()).toFile(),path.toFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return;
        }
        try {
            FileUtils.copy(Path.of("Storage","Templates","Servers",getName()).toFile(),path.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CompletableFuture<?> updateTemplate(){
        if (CloudAPI.getInstance().getModuleManager().isMainNode()) return new CompletableFuture<>().completeAsync(() -> true);

        if (!Path.of("Storage","Templates","Servers",getName()).toFile().mkdirs()){
            System.err.println("Could not create template directory.");
            return new CompletableFuture<>().completeAsync(() -> false);
        }

        return NetworkManager.getInstance().sendRequest(new GroupTemplateRequest(getName())).handle((packet, throwable) -> {
            GroupTemplateResponse templateResponse = packet.convert(GroupTemplateResponse.class);
            if (throwable != null){
                throwable.printStackTrace();
                return false;
            }

            System.out.println("Template updated: "+Path.of("Storage","Templates","Servers",getName()));
            return true;
        });
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
        nodeServer.setNode(optionalModuleMetrics.isEmpty() ? CloudAPI.getInstance().getModuleManager().getMainNode(): optionalModuleMetrics.get().getNetworkId());

        ServerCreateInfo serverCreateInfo = new ServerCreateInfo(nodeServer);
        NetworkManager.getInstance().sendPacket(serverCreateInfo);

        CloudAPI.getInstance().getServerManager().addServer(nodeServer);
        return nodeServer;
    }
}
