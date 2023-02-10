package de.curse.allround.core.cloud.server;

import de.curse.allround.core.cloud.servergroup.ServerGroup;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Data
@AllArgsConstructor
public abstract class Server {
    private final String name;

    private UUID node;
    private UUID networkId;
    private String status;
    private ServerGroup serverGroup;
    private boolean maintenance;
    private String joinPermissions;
    private String host;
    private int port;
    private boolean running;

    public void start(){
        if (serverGroup == null || serverGroup.getDefaultStartConfiguration() == null) return;
        start(serverGroup.getDefaultStartConfiguration());
    }

    public abstract CompletableFuture<Boolean> start(StartConfiguration startConfiguration);

    public abstract CompletableFuture<Boolean> stop();

    public abstract CompletableFuture<Boolean> kickAll(String reason);
}
