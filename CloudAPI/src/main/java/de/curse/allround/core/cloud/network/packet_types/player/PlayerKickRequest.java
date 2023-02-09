package de.curse.allround.core.cloud.network.packet_types.player;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketChannel;
import de.curse.allround.core.cloud.network.packet.PacketType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class PlayerKickRequest extends PacketType {

    private final UUID player;
    private final String reason;

    public PlayerKickRequest(Packet packet) {
        super(packet);
        this.player = UUID.fromString(packet.getData()[0]);
        this.reason = packet.getData()[1];
    }

    @Override
    public Packet toPacket() {
        return Packet.request(CloudAPI.getInstance().getPlayerManager().getPlayer(player).get().getProxy().getNetworkId(), "PLAYER_KICK_REQUEST", PacketChannel.CLOUD,new String[]{player.toString(),reason});
    }
}
