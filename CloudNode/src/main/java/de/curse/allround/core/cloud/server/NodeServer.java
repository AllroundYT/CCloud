package de.curse.allround.core.cloud.server;

import de.curse.allround.core.cloud.servergroup.ServerGroup;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class NodeServer extends Server{
    public NodeServer(String name, UUID node, UUID networkId, String status, ServerGroup serverGroup, boolean maintenance, String joinPermissions, String host, int port, boolean running) {
        super(name, node, networkId, status, serverGroup, maintenance, joinPermissions, host, port, running);
    }

    @Override
    public CompletableFuture<Boolean> start(StartConfiguration startConfiguration) {
        //TODO: send ServerStartRequest
        return null;
    }

    @Override
    public CompletableFuture<Boolean> stop() {
        //TODO: send ServerStopRequest
        return null;
    }

    @Override
    public CompletableFuture<Boolean> kickAll(String reason) {
        //TODO: send ServerKickAllRequest
        return null;
    }
}
