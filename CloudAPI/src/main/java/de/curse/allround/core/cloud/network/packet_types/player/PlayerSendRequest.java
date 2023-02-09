package de.curse.allround.core.cloud.network.packet_types.player;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketChannel;
import de.curse.allround.core.cloud.network.packet.PacketType;
import de.curse.allround.core.cloud.server.Server;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class PlayerSendRequest extends PacketType {
    private final UUID player;
    private final Server server;
    public PlayerSendRequest(Packet packet) {
        super(packet);
        this.player = UUID.fromString(packet.getData()[0]);
        this.server = CloudAPI.getInstance().getServerManager().getServer(packet.getData()[1]).orElse(null);
    }

    @Override
    public Packet toPacket() {
        return Packet.request(CloudAPI.getInstance().getPlayerManager().getPlayer(player).get().getProxy().getNetworkId(),"PLAYER_SEND_REQUEST", PacketChannel.CLOUD,new String[]{player.toString(), server.getName()});
    }
}
