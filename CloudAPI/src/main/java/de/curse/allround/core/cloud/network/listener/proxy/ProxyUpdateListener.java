package de.curse.allround.core.cloud.network.listener.proxy;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.listener.SpecificTypePacketListener;
import de.curse.allround.core.cloud.network.packet_types.proxy.ProxyUpdateInfo;
import de.curse.allround.core.cloud.proxy.Proxy;
import org.jetbrains.annotations.NotNull;

public class ProxyUpdateListener implements SpecificTypePacketListener<ProxyUpdateInfo> {
    @Override
    public void listen(@NotNull ProxyUpdateInfo packetType) {
        Proxy proxy = packetType.getProxy();
        if (CloudAPI.getInstance().getProxyManager().addProxy(proxy)){
            return;
        }
        Proxy foundProxy = CloudAPI.getInstance().getProxyManager().getProxy(proxy.getName()).get();

        foundProxy.setHost(proxy.getHost());
        foundProxy.setMaintenance(proxy.isMaintenance());
        foundProxy.setMaxPlayers(proxy.getMaxPlayers());
        foundProxy.setMaxRam(proxy.getMaxRam());
        foundProxy.setNetworkId(proxy.getNetworkId());
        foundProxy.setMotd(proxy.getMotd());
        foundProxy.setPort(proxy.getPort());
        foundProxy.setNode(proxy.getNode());
        foundProxy.setStatus(proxy.getStatus());
        foundProxy.setRunning(proxy.isRunning());
    }
}
