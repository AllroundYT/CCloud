package de.curse.allround.core.cloud.network.packet_types.module;

import de.curse.allround.core.cloud.module.Module;
import de.curse.allround.core.cloud.module.ModuleType;
import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * Wird gesendet, wenn ein neues Modul registriert wurde.
 */
@RequiredArgsConstructor
@Getter
public class ModuleConnectInfo extends PacketType {

    private final Module module;

    public ModuleConnectInfo(Packet packet) {
        super(packet);
        String name = packet.getData()[0];
        ModuleType moduleType = ModuleType.valueOf(packet.getData()[1]);
        UUID networkId = UUID.fromString(packet.getData()[2]);
        this.module = new Module(moduleType,networkId,name);
    }

    @Override
    public Packet toPacket() {
        return Packet.regular("MODULE_CONNECT_INFO", new String[]{module.getName(),module.getModuleType().name(),module.getNetworkId().toString()});
    }
}
