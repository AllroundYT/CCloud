package de.curse.allround.core.cloud.player;

import de.curse.allround.core.cloud.proxy.Proxy;
import de.curse.allround.core.cloud.server.Server;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class PlayerSnapshot extends Player{
    public PlayerSnapshot(UUID uuid, Server server, Proxy proxy) {
        super(uuid, server, proxy);
    }


    @Override
    public CompletableFuture<Boolean> send(Server server) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> sendMessage(String msg) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> kick(String reason) {
        return null;
    }
}
