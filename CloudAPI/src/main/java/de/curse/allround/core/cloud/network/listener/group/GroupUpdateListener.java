package de.curse.allround.core.cloud.network.listener.group;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.listener.SpecificTypePacketListener;
import de.curse.allround.core.cloud.network.packet_types.servergroup.GroupUpdateInfo;
import de.curse.allround.core.cloud.servergroup.ServerGroup;
import org.jetbrains.annotations.NotNull;

public class GroupUpdateListener implements SpecificTypePacketListener<GroupUpdateInfo> {
    @Override
    public void listen(@NotNull GroupUpdateInfo packetType) {
        ServerGroup serverGroup = packetType.getServerGroup();
        if (CloudAPI.getInstance().getServerGroupManager().addGroup(serverGroup)){
            return;
        }
        ServerGroup group = CloudAPI.getInstance().getServerGroupManager().getGroup(serverGroup.getName()).get();
        group.setIgnoredStates(serverGroup.getIgnoredStates());
        group.setMaxServers(serverGroup.getMaxServers());
        group.setMinServers(serverGroup.getMinServers());
        group.setDefaultStartConfiguration(serverGroup.getDefaultStartConfiguration());
    }
}
