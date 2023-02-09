package de.curse.allround.core.cloud.network.packet_types.player;

import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketChannel;
import de.curse.allround.core.cloud.network.packet.PacketType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class PlayerDisconnectInfo extends PacketType {

    private final UUID player;

    public PlayerDisconnectInfo(Packet packet) {
        super(packet);
        player = UUID.fromString(packet.getData()[0]);
    }

    @Override
    public Packet toPacket() {
        return Packet.regular("PLAYER_DISCONNECT_INFO", PacketChannel.CLOUD,new String[]{player.toString()});
    }
}
