package de.curse.allround.core.cloud.proxy;

import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet_types.proxy.ProxyUpdateInfo;
import de.curse.allround.core.cloud.network.packet_types.server.ServerUpdateInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public abstract class Proxy {
    private final String name;

    private UUID node;
    private UUID networkId;
    private String motd;
    private int maxRam;
    private int maxPlayers;
    private boolean maintenance;
    private String status;
    private String host;
    private int port;
    private boolean running;

    public void broadcastUpdate(){
        NetworkManager.getInstance().sendPacket(new ProxyUpdateInfo(this));
    }
    public abstract CompletableFuture<Boolean> start();
    public abstract CompletableFuture<Boolean> stop();
}
