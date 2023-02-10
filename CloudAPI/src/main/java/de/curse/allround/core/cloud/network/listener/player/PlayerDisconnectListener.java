package de.curse.allround.core.cloud.network.listener.player;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.listener.SpecificTypePacketListener;
import de.curse.allround.core.cloud.network.packet_types.player.PlayerDisconnectInfo;
import org.jetbrains.annotations.NotNull;

public class PlayerDisconnectListener implements SpecificTypePacketListener<PlayerDisconnectInfo> {
    @Override
    public void listen(@NotNull PlayerDisconnectInfo packetType) {
        if (CloudAPI.getInstance().getPlayerManager().getPlayer(packetType.getPlayer()).isEmpty()) return;
        CloudAPI.getInstance().getPlayerManager().getPlayer(packetType.getPlayer()).get().setServer(null);
        CloudAPI.getInstance().getPlayerManager().getPlayer(packetType.getPlayer()).get().setProxy(null);
    }
}
