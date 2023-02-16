package de.curse.allround.core.cloud.network.packet.listener;

import de.curse.allround.core.cloud.network.packet.Packet;

@FunctionalInterface
public interface PacketListener {
    void listen(Packet packet);
}
