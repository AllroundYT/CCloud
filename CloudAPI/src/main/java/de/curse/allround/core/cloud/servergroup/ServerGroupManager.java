package de.curse.allround.core.cloud.servergroup;

import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet_types.servergroup.GroupDeleteInfo;
import lombok.Data;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class ServerGroupManager {
    private final List<ServerGroup> serverGroups;
    private final Class<? extends ServerGroup> groupImplClass;

    @Contract(pure = true)
    public ServerGroupManager(Class<? extends ServerGroup> groupImplClass) {
        this.groupImplClass = groupImplClass;
        this.serverGroups = new CopyOnWriteArrayList<>();
    }

    public synchronized void deleteGroup(String group){
        if (getGroup(group).isEmpty()) return;
        GroupDeleteInfo groupDeleteInfo = new GroupDeleteInfo(group);
        NetworkManager.getInstance().sendPacket(groupDeleteInfo);
        removeGroup(group);
    }

    public synchronized boolean addGroup(ServerGroup serverGroup){
        if (serverGroups.stream().anyMatch(serverGroup1 -> serverGroup1.getName().equals(serverGroup.getName()))) return false;
        serverGroups.add(serverGroup);
        return true;
    }

    public synchronized Optional<ServerGroup> getGroup(String name){
        return serverGroups.stream().filter(serverGroup -> serverGroup.getName().equals(name)).findFirst();
    }

    public synchronized void removeGroup(String name){
        serverGroups.removeIf(serverGroup -> serverGroup.getName().equals(name));
    }
}
