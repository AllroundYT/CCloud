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
public class PlayerConnectInfo extends PacketType {

    private final Player player;

    public PlayerConnectInfo(Packet packet) {
        super(packet);

        UUID uuid = UUID.fromString(packet.getData()[0]);
        Server server = CloudAPI.getInstance().getServerManager().getServer(packet.getData()[1]).orElse(null);
        Proxy proxy = CloudAPI.getInstance().getProxyManager().getProxy(packet.getData()[2]).orElse(null);

        player = new PlayerSnapshot(uuid,server,proxy);
    }

    @Override
    public Packet toPacket() {
        return Packet.regular("PLAYER_CONNECT_INFO", player.getUuid().toString(),player.getServer().getName(),player.getProxy().getName());
    }
}
