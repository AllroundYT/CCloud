package de.curse.allround.core.cloud.server;

import de.curse.allround.core.cloud.servergroup.ServerGroup;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ServerSnapshot extends Server{


    public ServerSnapshot(String name, UUID node, UUID networkId, String status, ServerGroup serverGroup, boolean maintenance, String joinPermissions, String host, int port, boolean running) {
        super(name, node, networkId, status, serverGroup, maintenance, joinPermissions, host, port, running);
    }

    @Override
    public CompletableFuture<Boolean> start(StartConfiguration startConfiguration) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> stop() {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> kickAll(String reason) {
        return null;
    }
}
