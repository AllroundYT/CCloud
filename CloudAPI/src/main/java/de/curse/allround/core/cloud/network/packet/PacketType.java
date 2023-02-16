package de.curse.allround.core.cloud.network.packet;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Setter
public abstract class PacketType {
    @Contract(pure = true)
    public PacketType() {
    }

    @Contract(pure = true)
    public PacketType(@NotNull Packet packet) {
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
