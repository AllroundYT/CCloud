package de.curse.allround.core.cloud.network.packet_types.player;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class PlayerSendMessageRequest extends PacketType {
    private final UUID player;
    private final String message;
    public PlayerSendMessageRequest(Packet packet) {
        super(packet);
        this.player = UUID.fromString(packet.getData()[0]);
        this.message = packet.getData()[1];
    }

    @Override
    public Packet toPacket() {
        return Packet.request(CloudAPI.getInstance().getPlayerManager().getPlayer(player).get().getProxy().getNetworkId(),"PLAYER_SEND_MSG_REQUEST", new String[]{player.toString(),message});
    }
}
