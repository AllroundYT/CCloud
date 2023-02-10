package de.curse.allround.core.cloud.network.packet_types.player;

import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class PlayerSendMessageResponse extends PacketType {

    private final String result;
    private final UUID responseId;
    public PlayerSendMessageResponse(Packet packet) {
        super(packet);
        this.responseId = packet.getResponseId();
        this.result = packet.getData()[0];
    }

    @Override
    public Packet toPacket() {
        return Packet.response(responseId,"PLAYER_SEND_MSG_RESPONSE", new String[]{result });
    }
}
