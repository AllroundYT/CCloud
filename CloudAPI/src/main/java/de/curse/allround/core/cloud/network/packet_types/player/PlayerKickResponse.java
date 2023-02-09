package de.curse.allround.core.cloud.network.packet_types.player;

import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketChannel;
import de.curse.allround.core.cloud.network.packet.PacketType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class PlayerKickResponse extends PacketType {
    private final String result;
    private final UUID responseId;
    public PlayerKickResponse(Packet packet) {
        super(packet);
        this.responseId = packet.getResponseId();
        this.result = packet.getData()[0];
    }

    @Override
    public Packet toPacket() {
        return Packet.response(responseId,"PLAYER_KICK_RESPONSE", PacketChannel.CLOUD,new String[]{result});
    }
}
