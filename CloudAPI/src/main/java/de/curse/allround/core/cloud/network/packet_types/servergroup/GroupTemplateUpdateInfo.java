package de.curse.allround.core.cloud.network.packet_types.servergroup;

import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public class GroupTemplateUpdateInfo extends PacketType {
    private final String group;
    public GroupTemplateUpdateInfo(@NotNull Packet packet) {
        super(packet);
        this.group = packet.getData()[0];
    }

    @Override
    public Packet toPacket() {
        return Packet.regular("GROUP_TEMPLATE_UPDATE_INFO",group);
    }
}
