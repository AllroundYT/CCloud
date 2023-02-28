package de.curse.allround.core.cloud.server;

import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet_types.server.ServerDeleteInfo;
import lombok.Getter;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class ServerManager {
    private final List<Server> servers;
    private final Class<? extends Server> serverImplClass;

    @Contract(pure = true)
    public ServerManager(Class<? extends Server> serverImplClass) {
        this.serverImplClass = serverImplClass;
        this.servers = new CopyOnWriteArrayList<>();
    }

    public synchronized void deleteServer(String server) {
        if (getServer(server).isEmpty()) return;
        if (getServer(server).get().isRunning()) {
            getServer(server).get().stop().handleAsync((success, throwable) -> {
                if ((boolean)success) {
                    ServerDeleteInfo serverDeleteInfo = new ServerDeleteInfo(server);
                    NetworkManager.getInstance().sendPacket(serverDeleteInfo);
                    removeServer(server);
                }
                return success;
            });
        } else {
            ServerDeleteInfo serverDeleteInfo = new ServerDeleteInfo(server);
            NetworkManager.getInstance().sendPacket(serverDeleteInfo);
            removeServer(server);
        }
    }

    public synchronized Optional<Server> getServer(String name) {
        return servers.stream().filter(server -> server.getName().equals(name)).findFirst();
    }

    public synchronized boolean addServer(Server server) {
        if (servers.stream().anyMatch(server1 -> server1.getName().equals(server.getName()))) return false;
        servers.add(server);
        return true;
    }

    public synchronized void removeServer(String name) {
        servers.removeIf(server -> server.getName().equals(name));
    }
}
