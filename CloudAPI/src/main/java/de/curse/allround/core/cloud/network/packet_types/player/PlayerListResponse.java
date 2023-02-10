package de.curse.allround.core.cloud.network.packet_types.player;

import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import de.curse.allround.core.cloud.util.JsonUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class PlayerListResponse extends PacketType {
    private final UUID[] players;
    private final UUID responseId;

    public PlayerListResponse(Packet packet) {
        super(packet);
        this.responseId = packet.getResponseId();
        this.players = JsonUtil.GSON.fromJson(packet.getData()[0],UUID[].class);
    }

    @Override
    public Packet toPacket() {
        return Packet.response(responseId,"PLAYER_LIST_RESPONSE",JsonUtil.GSON.toJson(players));
    }
}
