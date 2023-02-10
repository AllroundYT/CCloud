package de.curse.allround.core.cloud.network.packet_types.player;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import de.curse.allround.core.cloud.server.Server;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class PlayerSwitchServerInfo extends PacketType {
    private final UUID player;
    private final Server server;
    public PlayerSwitchServerInfo(Packet packet) {
        super(packet);
        this.player = UUID.fromString(packet.getData()[0]);
        this.server = CloudAPI.getInstance().getServerManager().getServer(packet.getData()[1]).orElse(null);
    }

    @Override
    public Packet toPacket() {
        return Packet.regular("PLAYER_SWITCH_SERVER_INFO", new String[]{player.toString(),server.getName()});
    }
}
