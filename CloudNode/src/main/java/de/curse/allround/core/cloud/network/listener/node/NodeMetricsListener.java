package de.curse.allround.core.cloud.network.listener.node;

import de.curse.allround.core.cloud.module.ModuleMetrics;
import de.curse.allround.core.cloud.module.NodeMetrics;
import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet.listener.SpecificTypePacketListener;
import de.curse.allround.core.cloud.network.packet_types.module.ModuleMetricsRequest;
import de.curse.allround.core.cloud.network.packet_types.module.ModuleMetricsResponse;
import org.jetbrains.annotations.NotNull;

public class NodeMetricsListener implements SpecificTypePacketListener<ModuleMetricsRequest> {
    @Override
    public void listen(@NotNull ModuleMetricsRequest moduleMetricsRequest) {
        ModuleMetrics moduleMetrics = new NodeMetrics();
        ModuleMetricsResponse moduleMetricsResponse = new ModuleMetricsResponse(moduleMetricsRequest.toPacket().getRequestId(),moduleMetrics);
        NetworkManager.getInstance().sendPacket(moduleMetricsResponse);
    }
}
