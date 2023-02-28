package de.curse.allround.core.cloud.server;

import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet_types.server.ServerUpdateInfo;
import de.curse.allround.core.cloud.servergroup.ServerGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public abstract class Server {
    private final String name;

    private UUID node;
    private UUID networkId;
    private String status = "CREATED";
    private final ServerGroup serverGroup;
    private boolean maintenance;
    private String joinPermissions;
    private String host;
    private int port;
    private boolean running;

    public void start(){
        if (serverGroup == null || serverGroup.getDefaultStartConfiguration() == null) return;
        start(serverGroup.getDefaultStartConfiguration());
    }

    public void broadcastUpdate(){
        NetworkManager.getInstance().sendPacket(new ServerUpdateInfo(this));
    }

    public abstract CompletableFuture<?> start(StartConfiguration startConfiguration);

    public abstract CompletableFuture<?> stop();

    public abstract CompletableFuture<?> kickAll(String reason);
}
