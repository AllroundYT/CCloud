package de.curse.allround.core.cloud.player;

import de.curse.allround.core.cloud.proxy.Proxy;
import de.curse.allround.core.cloud.server.Server;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Data
@AllArgsConstructor
public abstract class Player {
    private final UUID uuid;

    private Server server;
    private Proxy proxy;

    public boolean isOnline(){
        return server != null && proxy != null;
    }

    public abstract CompletableFuture<Boolean> send(Server server);
    public abstract CompletableFuture<Boolean> sendMessage(String msg);
    public abstract CompletableFuture<Boolean> kick(String reason);
}
