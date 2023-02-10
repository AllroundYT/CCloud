package de.curse.allround.core.cloud.network.listener.player;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.listener.SpecificTypePacketListener;
import de.curse.allround.core.cloud.network.packet_types.player.PlayerSwitchServerInfo;
import org.jetbrains.annotations.NotNull;

public class PlayerSwitchServerListener implements SpecificTypePacketListener<PlayerSwitchServerInfo> {
    @Override
    public void listen(@NotNull PlayerSwitchServerInfo playerSwitchServerInfo) {
        if (CloudAPI.getInstance().getPlayerManager().getPlayer(playerSwitchServerInfo.getPlayer()).isEmpty()) return;
        CloudAPI.getInstance().getPlayerManager().getPlayer(playerSwitchServerInfo.getPlayer()).get().setServer(playerSwitchServerInfo.getServer());
    }
}
