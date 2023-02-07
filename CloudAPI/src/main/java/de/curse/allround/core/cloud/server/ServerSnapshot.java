package de.curse.allround.core.cloud.server;

import de.curse.allround.core.cloud.servergroup.ServerGroup;

public class ServerSnapshot extends Server{

    public ServerSnapshot(String name, String status, ServerGroup serverGroup, boolean maintenance, String joinPermissions, String host, int port) {
        super(name, status, serverGroup, maintenance, joinPermissions, host, port);
    }

    @Override
    public void start(StartConfiguration startConfiguration) {
    }

    @Override
    public void stop() {
    }

    @Override
    public void kickAll(String reason) {
    }
}
