package de.curse.allround.core.cloud.servergroup;

import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet_types.group.GroupUpdateInfo;
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

    public void broadcastUpdate(){
        NetworkManager.getInstance().sendPacket(new GroupUpdateInfo(this));
    }
    public abstract Server createServer();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerGroup that = (ServerGroup) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
