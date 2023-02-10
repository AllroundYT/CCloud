package de.curse.allround.core.cloud.network.packet_types.server;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ServerDataRequest extends PacketType {
    private final String server;
    public ServerDataRequest(Packet packet) {
        super(packet);
        this.server = packet.getData()[0];
    }

    @Override
    public Packet toPacket() {
        return Packet.request(CloudAPI.getInstance().getModuleManager().getMainNode(), "SERVER_DATA_REQUEST", server);
    }
}
