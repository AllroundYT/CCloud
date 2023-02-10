package de.curse.allround.core.cloud.network.listener.player;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.listener.SpecificTypePacketListener;
import de.curse.allround.core.cloud.network.packet_types.player.PlayerConnectInfo;

public class PlayerConnectListener implements SpecificTypePacketListener<PlayerConnectInfo> {
    @Override
    public void listen(PlayerConnectInfo packetType) {
        CloudAPI.getInstance().getPlayerManager().addPlayer(packetType.getPlayer());
        CloudAPI.getInstance().getPlayerManager().getPlayer(packetType.getPlayer().getUuid()).get().setProxy(packetType.getPlayer().getProxy());
        CloudAPI.getInstance().getPlayerManager().getPlayer(packetType.getPlayer().getUuid()).get().setServer(packetType.getPlayer().getServer());
    }
}
