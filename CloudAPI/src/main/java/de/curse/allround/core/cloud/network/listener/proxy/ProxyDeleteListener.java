package de.curse.allround.core.cloud.network.listener.proxy;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.listener.SpecificTypePacketListener;
import de.curse.allround.core.cloud.network.packet_types.proxy.ProxyDeleteInfo;
import org.jetbrains.annotations.NotNull;

public class ProxyDeleteListener implements SpecificTypePacketListener<ProxyDeleteInfo> {
    @Override
    public void listen(@NotNull ProxyDeleteInfo proxyDeleteInfo) {
        CloudAPI.getInstance().getProxyManager().removeProxy(proxyDeleteInfo.getProxy());
    }
}
