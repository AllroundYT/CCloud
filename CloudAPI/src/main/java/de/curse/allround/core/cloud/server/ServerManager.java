package de.curse.allround.core.cloud.server;

import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerManager {
    private final List<Server> servers;

    @Contract(pure = true)
    public ServerManager() {
        this.servers = new CopyOnWriteArrayList<>();
    }

    public Optional<Server> getServer(String name){
        return servers.stream().filter(server -> server.getName().equals(name)).findFirst();
    }

    public boolean addServer(Server server){
        if (servers.stream().anyMatch(server1 -> server1.getName().equals(server.getName()))) return false;
        servers.add(server);
        return true;
    }

    public void removeServer(String name){
        servers.removeIf(server -> server.getName().equals(name));
    }
}
