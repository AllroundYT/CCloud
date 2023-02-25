package de.curse.allround.core.cloud.network.packet_types.module;

import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class MainNodeSwitchInfo extends PacketType {

    private final UUID newMainNode;

    public MainNodeSwitchInfo(@NotNull Packet packet) {
        super(packet);
        this.newMainNode = UUID.fromString(packet.getData()[0]);
    }

    @Override
    public Packet toPacket() {
        return Packet.regular("MAIN_NODE_SWITCH_INFO", newMainNode.toString());
    }
}
