package de.curse.allround.core.cloud.network.packet_types.module;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public class ModuleListRequest extends PacketType {
    public ModuleListRequest(@NotNull Packet packet) {
        super(packet);
    }

    @Override
    public Packet toPacket() {
        return Packet.request(CloudAPI.getInstance().getModuleManager().getMainNode(), "MODULE_LIST_REQUEST");
    }
}
