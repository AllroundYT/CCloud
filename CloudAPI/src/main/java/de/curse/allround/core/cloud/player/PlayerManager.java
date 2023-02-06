package de.curse.allround.core.cloud.player;

import de.curse.allround.core.cloud.proxy.Proxy;
import de.curse.allround.core.cloud.server.Server;
import lombok.Data;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Data
public class PlayerManager {
    private final List<Player> players;

    @Contract(pure = true)
    public PlayerManager() {
        this.players = new CopyOnWriteArrayList<>();
    }

    public Optional<Player> getPlayer(UUID uuid){
        return players.stream().filter(player -> player.getUuid().equals(uuid)).findFirst();
    }

    public List<Player> getPlayers(Server server){
        return players.stream().filter(player -> player.getServer().equals(server)).collect(Collectors.toList());
    }

    public List<Player> getPlayers(Proxy proxy){
        return players.stream().filter(player -> player.getProxy().equals(proxy)).collect(Collectors.toList());
    }

    public boolean addPlayer(Player player){
        if (players.stream().anyMatch(player1 -> player.getUuid().equals(player1.getUuid()))) return false;
        players.add(player);
        return true;
    }

    public void removePlayer(UUID uuid){
        players.removeIf(player -> player.getUuid().equals(uuid));
    }
}