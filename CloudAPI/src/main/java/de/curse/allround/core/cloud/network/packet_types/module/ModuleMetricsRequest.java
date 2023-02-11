package de.curse.allround.core.cloud.network.packet_types.module;

import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ModuleMetricsRequest extends PacketType {
    private final UUID moduleId;

    public ModuleMetricsRequest(Packet packet) {
        super(packet);
        this.moduleId = packet.getReceiver();
    }

    @Override
    public Packet toPacket() {
        return Packet.request(moduleId,"MODULE_METRICS_REQUEST");
    }
}
