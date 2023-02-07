package de.curse.allround.core.cloud.player;

import de.curse.allround.core.cloud.proxy.Proxy;
import de.curse.allround.core.cloud.server.Server;

import java.util.UUID;

public class PlayerSnapshot extends Player{
    public PlayerSnapshot(UUID uuid, Server server, Proxy proxy) {
        super(uuid, server, proxy);
    }

    @Override
    public void send(Server server) {

    }

    @Override
    public void sendMessage(String msg) {

    }

    @Override
    public void kick(String reason) {

    }
}
