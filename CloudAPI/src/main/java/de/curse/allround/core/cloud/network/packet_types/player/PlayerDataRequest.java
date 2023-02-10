package de.curse.allround.core.cloud.network.packet_types.player;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class PlayerDataRequest extends PacketType {

    private final UUID player;
    public PlayerDataRequest(Packet packet) {
        super(packet);
        player = UUID.fromString(packet.getData()[0]);
    }

    @Override
    public Packet toPacket() {
        return Packet.request(CloudAPI.getInstance().getModuleManager().getMainNode(), "PLAYER_DATA_REQUEST", new String[]{player.toString()});
    }
}
