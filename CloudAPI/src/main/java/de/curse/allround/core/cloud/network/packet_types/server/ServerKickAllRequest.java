package de.curse.allround.core.cloud.network.packet_types.server;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ServerKickAllRequest extends PacketType {
    private final String server;
    private final String reason;
    public ServerKickAllRequest(Packet packet) {
        super(packet);
        this.server = packet.getData()[0];
        this.reason = packet.getData()[1];
    }

    @Override
    public Packet toPacket() {
        return Packet.request(CloudAPI.getInstance().getModuleManager().getMainNode(), "SERVER_KICK_ALL_REQUEST",server,reason);
    }
}
