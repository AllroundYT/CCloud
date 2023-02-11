package de.curse.allround.core.cloud.network.listener.proxy;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.listener.SpecificTypePacketListener;
import de.curse.allround.core.cloud.network.packet_types.proxy.ProxyCreateInfo;
import org.jetbrains.annotations.NotNull;

public class ProxyCreateListener implements SpecificTypePacketListener<ProxyCreateInfo> {
    @Override
    public void listen(@NotNull ProxyCreateInfo proxyCreateInfo) {
        CloudAPI.getInstance().getProxyManager().addProxy(proxyCreateInfo.getProxy());
    }
}
