package de.curse.allround.core.cloud.proxy;

import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet.PacketConverter;
import de.curse.allround.core.cloud.network.packet_types.proxy.ProxyStartRequest;
import de.curse.allround.core.cloud.network.packet_types.proxy.ProxyStartResponse;
import de.curse.allround.core.cloud.network.packet_types.proxy.ProxyStopRequest;
import de.curse.allround.core.cloud.network.packet_types.proxy.ProxyStopResponse;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class NodeProxy extends Proxy{


    public NodeProxy(String name, UUID node, UUID networkId, String motd, int maxRam, int maxPlayers, boolean maintenance, String status, String host, int port, boolean running) {
        super(name, node, networkId, motd, maxRam, maxPlayers, maintenance, status, host, port, running);
    }

    @Override
    public CompletableFuture<Boolean> start() {
        ProxyStartRequest proxyStartRequest = new ProxyStartRequest(getName());
        return NetworkManager.getInstance().sendRequest(proxyStartRequest).handleAsync((packet, throwable) -> {
            ProxyStartResponse proxyStartResponse = PacketConverter.getInstance().convert(packet);
            if (proxyStartResponse.getResult().equalsIgnoreCase("SUCCESS")){
                setRunning(true);
                return true;
            }
            return false;
        });
    }

    @Override
    public CompletableFuture<Boolean> stop() {
        ProxyStopRequest proxyStopRequest = new ProxyStopRequest(getName());
        return NetworkManager.getInstance().sendRequest(proxyStopRequest).handleAsync((packet, throwable) -> {
            ProxyStopResponse proxyStopResponse = PacketConverter.getInstance().convert(packet);
            if (proxyStopResponse.getResult().equalsIgnoreCase("SUCCESS")){
                setRunning(false);
                return true;
            }
            return false;
        });
    }
}
