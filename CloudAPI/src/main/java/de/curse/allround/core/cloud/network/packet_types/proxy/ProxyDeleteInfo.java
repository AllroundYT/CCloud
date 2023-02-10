package de.curse.allround.core.cloud.network.packet_types.proxy;

import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ProxyDeleteInfo extends PacketType {
    private final String proxy;
    public ProxyDeleteInfo(Packet packet) {
        super(packet);
        proxy = packet.getData()[0];
    }

    @Override
    public Packet toPacket() {
        return Packet.regular("PROXY_DELETE_INFO",proxy);
    }
}
