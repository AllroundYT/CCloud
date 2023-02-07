package de.curse.allround.core.cloud.servergroup;

import de.curse.allround.core.cloud.server.Server;
import de.curse.allround.core.cloud.server.StartConfiguration;

import java.util.Set;

public class ServerGroupSnapshot extends ServerGroup{
    public ServerGroupSnapshot(String name, int minServers, int maxServers, Set<String> ignoredStates, StartConfiguration defaultStartConfiguration) {
        super(name, minServers, maxServers, ignoredStates, defaultStartConfiguration);
    }

    @Override
    public Server createServer() {
        return null;
    }
}
