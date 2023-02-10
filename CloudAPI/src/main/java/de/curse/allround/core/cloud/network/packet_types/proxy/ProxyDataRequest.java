package de.curse.allround.core.cloud.network.packet_types.proxy;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ProxyDataRequest extends PacketType {

    private final String proxy;

    public ProxyDataRequest(Packet packet) {
        super(packet);
        this.proxy = packet.getData()[0];
    }

    @Override
    public Packet toPacket() {
        return Packet.request(CloudAPI.getInstance().getModuleManager().getMainNode(), "PROXY_DATA_REQUEST",proxy);
    }
}
