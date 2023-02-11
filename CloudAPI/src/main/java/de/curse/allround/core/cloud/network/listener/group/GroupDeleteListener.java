package de.curse.allround.core.cloud.network.listener.group;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.listener.SpecificTypePacketListener;
import de.curse.allround.core.cloud.network.packet_types.servergroup.GroupDeleteInfo;
import org.jetbrains.annotations.NotNull;

public class GroupDeleteListener implements SpecificTypePacketListener<GroupDeleteInfo> {
    @Override
    public void listen(@NotNull GroupDeleteInfo packetType) {
        CloudAPI.getInstance().getServerGroupManager().removeGroup(packetType.getGroup());
    }
}
