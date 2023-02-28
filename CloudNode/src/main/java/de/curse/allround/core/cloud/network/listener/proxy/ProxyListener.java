package de.curse.allround.core.cloud.network.listener.proxy;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.PacketBus;
import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet_types.proxy.ProxyCreateInfo;
import de.curse.allround.core.cloud.network.packet_types.proxy.ProxyStartRequest;
import de.curse.allround.core.cloud.network.packet_types.proxy.ProxyStartResponse;
import de.curse.allround.core.cloud.proxy.NodeProxy;

public class ProxyListener {
    public void register(){
        PacketBus packetBus = NetworkManager.getInstance().getPacketBus();

        packetBus.listen("PROXY_CREATE_INFO", packet -> {
            ProxyCreateInfo proxyCreateInfo = packet.convert(ProxyCreateInfo.class);
            if (proxyCreateInfo.getProxy().getNode().equals(CloudAPI.getInstance().getModuleManager().getThisModule().getNetworkId())){
                NodeProxy nodeProxy = (NodeProxy) proxyCreateInfo.getProxy();
                nodeProxy.copyTemplate();
            }
        });

        packetBus.listen("PROXY_START_REQUEST", packet -> {
            ProxyStartRequest proxyStartRequest = packet.convert(ProxyStartRequest.class);
            if (!CloudAPI.getInstance().getProxyManager().getProxy(proxyStartRequest.getProxy()).get().getNode().equals(CloudAPI.getInstance().getModuleManager().getThisModule().getNetworkId())) return;
            NodeProxy nodeProxy = (NodeProxy) CloudAPI.getInstance().getProxyManager().getProxy(proxyStartRequest.getProxy()).get();
            nodeProxy.startProcess();
            ProxyStartResponse proxyStartResponse = new ProxyStartResponse(packet.getRequestId(),"SUCCESS");
        });
    }
}
