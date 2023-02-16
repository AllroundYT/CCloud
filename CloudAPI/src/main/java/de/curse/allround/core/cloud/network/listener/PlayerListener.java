package de.curse.allround.core.cloud.network.listener;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.EventBus;
import de.curse.allround.core.cloud.network.packet_types.player.*;
import de.curse.allround.core.cloud.player.Player;

import java.util.UUID;

public class PlayerListener {
    public void register() {
        EventBus eventBus = CloudAPI.getInstance().getNetworkManager().getEventBus();

        eventBus.listen("PLAYER_LIST_REQUEST", packet -> {
            PlayerListResponse playerListResponse = new PlayerListResponse(
                    packet.getRequestId(),
                    CloudAPI.getInstance().getPlayerManager().getPlayers().stream().map(Player::getUuid).toList().toArray(new UUID[0])
            );
        });

        eventBus.listen("PLAYER_DATA_REQUEST", packet -> {
            PlayerDataRequest playerDataRequest = packet.convert(PlayerDataRequest.class);
            PlayerDataResponse playerDataResponse = new PlayerDataResponse(
                    packet.getRequestId(),
                    CloudAPI.getInstance().getPlayerManager().getPlayer(playerDataRequest.getPlayer()).get()
            );
        });

        eventBus.listen("PLAYER_CONNECT_INFO",packet -> {
            PlayerConnectInfo playerConnectInfo = packet.convert(PlayerConnectInfo.class);
            CloudAPI.getInstance().getPlayerManager().addPlayer(playerConnectInfo.getPlayer());
            CloudAPI.getInstance().getPlayerManager().getPlayer(playerConnectInfo.getPlayer().getUuid()).get().setProxy(playerConnectInfo.getPlayer().getProxy());
            CloudAPI.getInstance().getPlayerManager().getPlayer(playerConnectInfo.getPlayer().getUuid()).get().setServer(playerConnectInfo.getPlayer().getServer());
        });

        eventBus.listen("PLAYER_DISCONNECT_INFO",packet -> {
            if (CloudAPI.getInstance().getPlayerManager().getPlayer(packet.convert(PlayerDisconnectInfo.class).getPlayer()).isEmpty()) return;
            CloudAPI.getInstance().getPlayerManager().getPlayer(packet.convert(PlayerDisconnectInfo.class).getPlayer()).get().setServer(null);
            CloudAPI.getInstance().getPlayerManager().getPlayer(packet.convert(PlayerDisconnectInfo.class).getPlayer()).get().setProxy(null);
        });

        eventBus.listen("PLAYER_SWITCH_SERVER_INFO",packet -> {
            if (CloudAPI.getInstance().getPlayerManager().getPlayer(packet.convert(PlayerSwitchServerInfo.class).getPlayer()).isEmpty()) return;
            CloudAPI.getInstance().getPlayerManager().getPlayer(packet.convert(PlayerSwitchServerInfo.class).getPlayer()).get().setServer(packet.convert(PlayerSwitchServerInfo.class).getServer());
        });
    }
}
