package de.curse.allround.core.cloud.network.listener;

import de.curse.allround.core.beta.network.listener.SpecificTypePacketListener;
import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet_types.module.ModuleConnectInfo;
import org.jetbrains.annotations.NotNull;

public class ModuleConnectListener implements SpecificTypePacketListener<ModuleConnectInfo> {

    @Override
    public void listen(@NotNull ModuleConnectInfo moduleConnectInfo) {
        CloudAPI.getInstance().getModuleManager().addModule(moduleConnectInfo.getModule());
    }
}
