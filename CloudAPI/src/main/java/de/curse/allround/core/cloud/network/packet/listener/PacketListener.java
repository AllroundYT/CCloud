package de.curse.allround.core.cloud.network.packet.listener;

import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketChannel;

@FunctionalInterface
public interface PacketListener {
    void listen(String type, PacketChannel channel, Packet packet);
}
