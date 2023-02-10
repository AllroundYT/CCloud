package de.curse.allround.core.cloud.network.packet_types.server;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import de.curse.allround.core.cloud.server.Server;
import de.curse.allround.core.cloud.server.ServerSnapshot;
import de.curse.allround.core.cloud.servergroup.ServerGroup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ServerDataResponse extends PacketType {
    private final UUID responseId;
    private final Server server;
    public ServerDataResponse(Packet packet) {
        super(packet);
        this.responseId = packet.getResponseId();

        String name = packet.getData()[0];
        UUID node = UUID.fromString(packet.getData()[1]);
        UUID networkId = UUID.fromString(packet.getData()[2]);
        String status = packet.getData()[3];
        ServerGroup serverGroup = CloudAPI.getInstance().getServerGroupManager().getGroup(packet.getData()[4]).orElse(null);
        boolean maintenance = Boolean.parseBoolean(packet.getData()[5]);
        String joinPermissions = packet.getData()[6];
        String host = packet.getData()[7];
        int port = Integer.parseInt(packet.getData()[8]);
        boolean running = Boolean.parseBoolean(packet.getData()[9]);

        server = new ServerSnapshot(name, node, networkId, status, serverGroup, maintenance, joinPermissions, host, port, running);
    }

    @Override
    public Packet toPacket() {
        return Packet.response(
                responseId,
                "SERVER_DATA_RESPONSE",
                server.getName(),
                String.valueOf(server.getNode()),
                String.valueOf(server.getNetworkId()),
                server.getStatus(),
                server.getServerGroup().getName(),
                String.valueOf(server.isMaintenance()),
                server.getJoinPermissions(),
                server.getHost(),
                String.valueOf(server.getPort()),
                String.valueOf(server.isRunning())
        );
    }
}
