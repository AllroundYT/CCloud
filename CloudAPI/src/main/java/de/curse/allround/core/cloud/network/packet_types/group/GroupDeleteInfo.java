package de.curse.allround.core.cloud.network.packet_types.group;

import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class GroupDeleteInfo extends PacketType {
    private final String group;
    public GroupDeleteInfo(Packet packet) {
        super(packet);
        this.group = packet.getData()[0];
    }

    @Override
    public Packet toPacket() {
        return Packet.regular("GROUP_DELETE_INFO",group);
    }
}
