package de.curse.allround.core.cloud.network.packet_types.proxy;

import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import de.curse.allround.core.cloud.util.JsonUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ProxyListResponse extends PacketType {
    private final UUID responseId;
    private final String[] proxies;
    public ProxyListResponse(Packet packet) {
        super(packet);
        this.responseId = packet.getResponseId();
        this.proxies = JsonUtil.GSON.fromJson(packet.getData()[0],String[].class);
    }

    @Override
    public Packet toPacket() {
        return Packet.response(responseId,"PROXY_LIST_RESPONSE",JsonUtil.GSON.toJson(proxies));
    }
}
