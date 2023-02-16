package de.curse.allround.core.cloud.network.packet_types.module;

import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import de.curse.allround.core.cloud.util.JsonUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ModuleListResponse extends PacketType {
    private final UUID responseId;
    private final UUID[] modules;

    public ModuleListResponse(@NotNull Packet packet) {
        super(packet);
        this.responseId = packet.getResponseId();
        this.modules = JsonUtil.GSON.fromJson(packet.getData()[0],UUID[].class);
    }

    @Override
    public Packet toPacket() {
        return Packet.response(responseId,"MODULE_LIST_RESPONSE",JsonUtil.GSON.toJson(modules));
    }
}
