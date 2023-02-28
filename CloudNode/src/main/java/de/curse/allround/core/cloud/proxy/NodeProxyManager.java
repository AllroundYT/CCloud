package de.curse.allround.core.cloud.proxy;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.CloudNode;
import de.curse.allround.core.cloud.module.ModuleInfo;
import de.curse.allround.core.cloud.module.NodeInfo;
import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet.PacketConverter;
import de.curse.allround.core.cloud.network.packet_types.module.ModuleDataRequest;
import de.curse.allround.core.cloud.network.packet_types.module.ModuleDataResponse;
import de.curse.allround.core.cloud.network.packet_types.proxy.ProxyCreateInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NodeProxyManager extends ProxyManager{

    private final ScheduledExecutorService scheduledExecutorService;
    public NodeProxyManager(Class<? extends Proxy> proxyImplClass) {
        super(proxyImplClass);
        this.scheduledExecutorService = Executors.newScheduledThreadPool(0);
    }

    @Override
    public Proxy createProxy() {
        Proxy proxy = new NodeProxy("Proxy-"+getProxies().size());

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
        proxy.setNode(optionalModuleMetrics.isEmpty() ? CloudAPI.getInstance().getModuleManager().getMainNode(): optionalModuleMetrics.get().getNetworkId());

        ProxyCreateInfo proxyCreateInfo = new ProxyCreateInfo(proxy);
        NetworkManager.getInstance().sendPacket(proxyCreateInfo);

        addProxy(proxy);

        return proxy;
    }

    public void start(){
        for (int i = 0 ; i < Integer.getInteger("node.proxies-to-start",1);i++){
            NodeProxy proxy = (NodeProxy) createProxy();
            proxy.start();
        }

        if (!CloudAPI.getInstance().getModuleManager().isMainNode()){
            return;
        }

        scheduledExecutorService.scheduleAtFixedRate(this::update,1,1, TimeUnit.MINUTES);
    }

    public void stop(){
        scheduledExecutorService.shutdownNow();
    }

    /**
     * Wird von main node ausgeführt um zu überwachen, dass immer ein Proxy online ist
     */
    public void update(){
        if (!CloudAPI.getInstance().getModuleManager().isMainNode()) return;
        if (getProxies().stream().filter(Proxy::isRunning).toList().size() > 0) return;
        NodeProxy proxy = (NodeProxy) createProxy();
        proxy.start();
    }
}
