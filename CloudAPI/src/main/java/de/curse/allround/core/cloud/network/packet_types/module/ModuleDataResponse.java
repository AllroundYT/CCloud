package de.curse.allround.core.cloud.network.packet_types.module;

import de.curse.allround.core.cloud.module.ModuleInfo;
import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import de.curse.allround.core.cloud.util.JsonUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ModuleDataResponse extends PacketType {
    private final UUID responseId;
    private final ModuleInfo moduleInfo;

    public ModuleDataResponse(Packet packet) {
        super(packet);
        this.responseId = packet.getResponseId();
        this.moduleInfo = JsonUtil.GSON.fromJson(packet.getData()[0], ModuleInfo.class);
    }

    @Override
    public Packet toPacket() {
        return Packet.response(responseId,"MODULE_DATA_RESPONSE", JsonUtil.GSON.toJson(moduleInfo));
    }
}
