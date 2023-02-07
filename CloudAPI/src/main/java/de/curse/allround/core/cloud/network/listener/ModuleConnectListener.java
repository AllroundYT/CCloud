package de.curse.allround.core.cloud.network.listener;

import de.curse.allround.core.beta.network.PacketType;
import de.curse.allround.core.beta.network.listener.SpecificTypePacketListener;
import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet_types.module.ModuleConnectedInfo;

public class ModuleConnectListener implements SpecificTypePacketListener {
    @Override
    public <T extends PacketType> void listen(T t) {
        ModuleConnectedInfo connectedInfo = (ModuleConnectedInfo) t;
        CloudAPI.getInstance().getModuleManager().addModule(connectedInfo.getModule());
    }
}
