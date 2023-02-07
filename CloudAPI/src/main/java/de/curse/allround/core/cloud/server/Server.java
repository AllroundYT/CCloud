package de.curse.allround.core.cloud.server;

import de.curse.allround.core.cloud.servergroup.ServerGroup;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class Server {
    private final String name;

    private String status;
    private ServerGroup serverGroup;
    private boolean maintenance;
    private String joinPermissions;
    private String host;
    private int port;

    public void start(){
        if (serverGroup == null || serverGroup.getDefaultStartConfiguration() == null) return;
        start(serverGroup.getDefaultStartConfiguration());
    }

    public abstract void start(StartConfiguration startConfiguration);

    public abstract void stop();

    public abstract void kickAll(String reason);
}
