package de.curse.allround.core.cloud.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class Proxy {
    private final String name;

    private String motd;
    private int maxRam;
    private int maxPlayers;
    private boolean maintenance;
    private String status;
    private String host;
    private int port;

    public abstract void start();
    public abstract void stop();
}
