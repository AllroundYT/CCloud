package de.curse.allround.core.cloud.player;

import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet.PacketConverter;
import de.curse.allround.core.cloud.network.packet_types.player.*;
import de.curse.allround.core.cloud.proxy.Proxy;
import de.curse.allround.core.cloud.server.Server;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class NodePlayer extends Player{
    public NodePlayer(UUID uuid, Server server, Proxy proxy) {
        super(uuid, server, proxy);
    }

    @Override
    public CompletableFuture<Boolean> send(Server server) {
        PlayerSendRequest playerSendRequest = new PlayerSendRequest(getUuid(),server);
        return NetworkManager.getInstance().sendRequest(playerSendRequest).handleAsync((packet, throwable) -> {
            PlayerSendResponse playerSendResponse = PacketConverter.getInstance().convert(packet);
            if (playerSendResponse.getResult().equalsIgnoreCase("SUCCESS")){
                setServer(server);
                return true;
            }
            return false;
        });
    }

    @Override
    public CompletableFuture<Boolean> sendMessage(String msg) {
        PlayerSendMessageRequest playerSendMessageRequest = new PlayerSendMessageRequest(getUuid(),msg);
        return NetworkManager.getInstance().sendRequest(playerSendMessageRequest).handleAsync((packet, throwable) -> {
            PlayerSendMessageResponse playerSendMessageResponse = PacketConverter.getInstance().convert(packet);
            return playerSendMessageResponse.getResult().equalsIgnoreCase("SUCCESS");
        });
    }

    @Override
    public CompletableFuture<Boolean> kick(String reason) {
        PlayerKickRequest playerKickRequest = new PlayerKickRequest(getUuid(),reason);
        return NetworkManager.getInstance().sendRequest(playerKickRequest).handleAsync((packet, throwable) -> {
            PlayerKickResponse playerKickResponse = PacketConverter.getInstance().convert(packet);
            if (playerKickResponse.getResult().equalsIgnoreCase("SUCCESS")){
                setServer(null);
                setProxy(null);
                return true;
            }
            return false;
        });
    }
}
