package de.curse.allround.core.cloud.network.listener;

import de.curse.allround.core.beta.network.listener.SpecificTypePacketListener;
import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet_types.module.ModuleDisconnectInfo;

public class ModuleDisconnectListener implements SpecificTypePacketListener<ModuleDisconnectInfo> {

    @Override
    public void listen(ModuleDisconnectInfo moduleDisconnectInfo) {
        CloudAPI.getInstance().getModuleManager().removeModule(moduleDisconnectInfo.getModule().getNetworkId());
    }
}
