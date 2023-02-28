package de.curse.allround.core.cloud.network.packet_types.group;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class GroupDataRequest extends PacketType {
    private final String group;
    public GroupDataRequest(Packet packet) {
        super(packet);
        this.group = packet.getData()[0];
    }

    @Override
    public Packet toPacket() {
        return Packet.request(CloudAPI.getInstance().getModuleManager().getMainNode(), "GROUP_DATA_REQUEST",group);
    }
}
