package de.curse.allround.core.cloud.proxy;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ProxySnapshot extends Proxy{


    public ProxySnapshot(String name, UUID node, UUID networkId, String motd, int maxRam, int maxPlayers, boolean maintenance, String status, String host, int port, boolean running) {
        super(name, node, networkId, motd, maxRam, maxPlayers, maintenance, status, host, port, running);
    }

    @Override
    public CompletableFuture<Boolean> start() {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> stop() {
        return null;
    }
}
