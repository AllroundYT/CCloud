package de.curse.allround.core.cloud.network.packet_types.servergroup;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public class GroupTemplateRequest extends PacketType {

    private final String group;
    public GroupTemplateRequest(@NotNull Packet packet) {
        super(packet);
        this.group = packet.getData()[0];
    }

    @Override
    public Packet toPacket() {
        return Packet.request(CloudAPI.getInstance().getModuleManager().getMainNode(), "GROUP_TEMPLATE_REQUEST",group);
    }
}
