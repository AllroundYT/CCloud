package de.curse.allround.core.cloud.network.packet_types.player;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PlayerListRequest extends PacketType {

    public PlayerListRequest(Packet packet) {
        super(packet);
    }

    @Override
    public Packet toPacket() {
        return Packet.request(CloudAPI.getInstance().getModuleManager().getMainNode(), "PLAYER_LIST_REQUEST");
    }
}
