package de.curse.allround.core.cloud.player;

import de.curse.allround.core.cloud.proxy.Proxy;
import de.curse.allround.core.cloud.server.Server;
import lombok.Data;

import java.util.UUID;

@Data
public abstract class Player {
    private final UUID uuid;

    private Server server;
    private Proxy proxy;

    public boolean isOnline(){
        return server != null && proxy != null;
    }

    public abstract void send(Server server);
    public abstract void sendMessage(String msg);
    public abstract void kick(String reason);
}
