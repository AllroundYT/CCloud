package de.curse.allround.core.cloud.servergroup;

import de.curse.allround.core.cloud.server.Server;
import de.curse.allround.core.cloud.server.StartConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public abstract class ServerGroup {
    private final String name;
    private int minServers;
    private int maxServers;
    private Set<String> ignoredStates;
    private StartConfiguration defaultStartConfiguration;

    public abstract Server createServer();
}
