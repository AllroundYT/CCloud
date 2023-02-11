package de.curse.allround.core.cloud.network.listener.group;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.listener.SpecificTypePacketListener;
import de.curse.allround.core.cloud.network.packet_types.servergroup.GroupCreateInfo;
import org.jetbrains.annotations.NotNull;

public class GroupCreateListener implements SpecificTypePacketListener<GroupCreateInfo> {
    @Override
    public void listen(@NotNull GroupCreateInfo packetType) {
        CloudAPI.getInstance().getServerGroupManager().addGroup(packetType.getServerGroup());
    }
}
