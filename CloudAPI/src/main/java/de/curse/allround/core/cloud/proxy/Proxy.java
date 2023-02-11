package de.curse.allround.core.cloud.proxy;

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

    public abstract CompletableFuture<Boolean> start();
    public abstract CompletableFuture<Boolean> stop();
}
