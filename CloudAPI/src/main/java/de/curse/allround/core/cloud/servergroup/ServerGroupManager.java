package de.curse.allround.core.cloud.servergroup;

import lombok.Data;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class ServerGroupManager {
    private final List<ServerGroup> serverGroups;

    @Contract(pure = true)
    public ServerGroupManager() {
        this.serverGroups = new CopyOnWriteArrayList<>();
    }

    public boolean addGroup(ServerGroup serverGroup){
        if (serverGroups.stream().anyMatch(serverGroup1 -> serverGroup1.getName().equals(serverGroup.getName()))) return false;
        serverGroups.add(serverGroup);
        return true;
    }

    public Optional<ServerGroup> getGroup(String name){
        return serverGroups.stream().filter(serverGroup -> serverGroup.getName().equals(name)).findFirst();
    }

    public void removeGroup(String name){
        serverGroups.removeIf(serverGroup -> serverGroup.getName().equals(name));
    }
}
