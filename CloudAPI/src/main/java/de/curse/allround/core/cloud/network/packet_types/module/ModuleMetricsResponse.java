package de.curse.allround.core.cloud.network.packet_types.module;

import de.curse.allround.core.cloud.module.ModuleMetrics;
import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import de.curse.allround.core.cloud.util.JsonUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ModuleMetricsResponse extends PacketType {
    private final UUID responseId;
    private final ModuleMetrics moduleMetrics;

    public ModuleMetricsResponse(Packet packet) {
        super(packet);
        this.responseId = packet.getResponseId();
        this.moduleMetrics = JsonUtil.GSON.fromJson(packet.getData()[0],ModuleMetrics.class);
    }

    @Override
    public Packet toPacket() {
        return Packet.response(responseId,"MODULE_METRICS_RESPONSE", JsonUtil.GSON.toJson(moduleMetrics));
    }
}
