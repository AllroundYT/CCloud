package de.curse.allround.core.cloud.network.listener;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.EventBus;
import de.curse.allround.core.cloud.network.packet_types.proxy.ProxyCreateInfo;
import de.curse.allround.core.cloud.network.packet_types.proxy.ProxyDeleteInfo;
import de.curse.allround.core.cloud.network.packet_types.proxy.ProxyUpdateInfo;
import de.curse.allround.core.cloud.proxy.Proxy;

public class ProxyListener {
    public void register(){
        EventBus eventBus = CloudAPI.getInstance().getNetworkManager().getEventBus();

        eventBus.listen("PROXY_CREATE_INFO",packet -> {
            CloudAPI.getInstance().getProxyManager().addProxy(packet.convert(ProxyCreateInfo.class).getProxy());
        });
        eventBus.listen("PROXY_DELETE_INFO",packet -> {
            CloudAPI.getInstance().getProxyManager().removeProxy(packet.convert(ProxyDeleteInfo.class).getProxy());
        });
        eventBus.listen("PROXY_UPDATE_INFO",packet -> {
            Proxy proxy = packet.convert(ProxyUpdateInfo.class).getProxy();
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
        });

        eventBus.listen("PROXY_DATA_REQUEST",packet -> {

        });
        eventBus.listen("PROXY_LIST_REQUEST",packet -> {

        });
    }
}
