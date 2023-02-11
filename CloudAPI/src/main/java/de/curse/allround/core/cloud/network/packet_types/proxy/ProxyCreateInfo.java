package de.curse.allround.core.cloud.network.packet_types.proxy;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import de.curse.allround.core.cloud.proxy.Proxy;
import de.curse.allround.core.cloud.proxy.ProxySnapshot;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ProxyCreateInfo extends PacketType {
    private final Proxy proxy;
    public ProxyCreateInfo(Packet packet) {
        super(packet);

        String name = packet.getData()[0];
        UUID node = UUID.fromString(packet.getData()[1]);
        UUID networkId = UUID.fromString(packet.getData()[2]);
        String motd = packet.getData()[3];
        int maxRam = Integer.parseInt(packet.getData()[4]);
        int maxPlayers = Integer.parseInt(packet.getData()[5]);
        boolean maintenance = Boolean.parseBoolean(packet.getData()[6]);
        String status = packet.getData()[7];
        String host = packet.getData()[8];
        int port = Integer.parseInt(packet.getData()[9]);
        boolean running = Boolean.parseBoolean(packet.getData()[10]);


        this.proxy = CloudAPI.getInstance().getProxyManager().getProxyImplClass().cast(new ProxySnapshot(name,node,networkId,motd,maxRam,maxPlayers,maintenance,status,host,port,running));
    }

    @Override
    public Packet toPacket() {
        return Packet.regular("PROXY_CREATE_INFO",
                proxy.getName(),
                proxy.getNode().toString(),
                proxy.getNetworkId().toString(),
                proxy.getMotd(),
                String.valueOf(proxy.getMaxRam()),
                String.valueOf(proxy.getMaxPlayers()),
                String.valueOf(proxy.isMaintenance()),
                proxy.getStatus(),
                proxy.getHost(),
                String.valueOf(proxy.getPort()),
                String.valueOf(proxy.isRunning()));
    }
}
