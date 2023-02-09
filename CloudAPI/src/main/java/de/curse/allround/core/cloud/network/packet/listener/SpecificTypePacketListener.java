package de.curse.allround.core.cloud.network.packet.listener;

import de.curse.allround.core.cloud.network.packet.PacketType;

@FunctionalInterface
public interface SpecificTypePacketListener<T extends PacketType> {
    void listen(T packetType);
}
