package de.curse.allround.core.cloud.network.packet_types.player;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import de.curse.allround.core.cloud.player.Player;
import de.curse.allround.core.cloud.player.PlayerSnapshot;
import de.curse.allround.core.cloud.proxy.Proxy;
import de.curse.allround.core.cloud.server.Server;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class PlayerDataResponse extends PacketType {

    private final Player player;
    private final UUID responseId;

    public PlayerDataResponse(Packet packet) {
        super(packet);
        this.responseId = packet.getResponseId();
        UUID playerId = UUID.fromString(packet.getData()[0]);
        Server server = CloudAPI.getInstance().getServerManager().getServer(packet.getData()[1]).orElse(null);
        Proxy proxy = CloudAPI.getInstance().getProxyManager().getProxy(packet.getData()[2]).orElse(null);
        this.player = CloudAPI.getInstance().getPlayerManager().getPlayerImplClass().cast(new PlayerSnapshot(playerId, server, proxy));
    }

    @Override
    public Packet toPacket() {
        return Packet.response(responseId, "PLAYER_DATA_RESPONSE",  new String[]{player.getUuid().toString(), player.getServer() != null ? player.getServer().getName() : "", player.getProxy() != null ? player.getProxy().getName() : ""});
    }
}
