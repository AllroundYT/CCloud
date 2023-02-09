package de.curse.allround.core.cloud.network.packet;

import org.jetbrains.annotations.Contract;

public abstract class PacketType {

    @Contract(pure = true)
    public PacketType() {
    }

    @Contract(pure = true)
    public PacketType(Packet packet) {
    }

    public abstract Packet toPacket();

    public static class NULL_TYPE extends PacketType {
        public NULL_TYPE(Packet packet) {
            super(packet);
        }

        @Override
        public Packet toPacket() {
            return null;
        }
    }
}
