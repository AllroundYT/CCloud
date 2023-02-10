package de.curse.allround.core.cloud.network.packet_types.proxy;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ProxyStopRequest extends PacketType {
    private final String proxy;
    public ProxyStopRequest(Packet packet) {
        super(packet);
        this.proxy = packet.getData()[0];
    }

    @Override
    public Packet toPacket() {
        return Packet.request(CloudAPI.getInstance().getProxyManager().getProxy(proxy).get().getNode(),"PROXY_STOP_REQUEST",proxy);
    }
}
