package de.curse.allround.core.cloud.network.listener;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.EventBus;
import de.curse.allround.core.cloud.network.packet_types.servergroup.GroupCreateInfo;
import de.curse.allround.core.cloud.network.packet_types.servergroup.GroupDeleteInfo;
import de.curse.allround.core.cloud.network.packet_types.servergroup.GroupUpdateInfo;
import de.curse.allround.core.cloud.servergroup.ServerGroup;

public class GroupListener {
    public void register(){
        EventBus eventBus = CloudAPI.getInstance().getNetworkManager().getEventBus();

        eventBus.listen("GROUP_CREATE_INFO",packet -> {
            CloudAPI.getInstance().getServerGroupManager().addGroup(packet.convert(GroupCreateInfo.class).getServerGroup());
        });
        eventBus.listen("GROUP_UPDATE_INFO",packet -> {
            ServerGroup serverGroup = packet.convert(GroupUpdateInfo.class).getServerGroup();
            if (CloudAPI.getInstance().getServerGroupManager().addGroup(serverGroup)){
                return;
            }
            ServerGroup group = CloudAPI.getInstance().getServerGroupManager().getGroup(serverGroup.getName()).get();
            group.setIgnoredStates(serverGroup.getIgnoredStates());
            group.setMaxServers(serverGroup.getMaxServers());
            group.setMinServers(serverGroup.getMinServers());
            group.setDefaultStartConfiguration(serverGroup.getDefaultStartConfiguration());
        });
        eventBus.listen("GROUP_DELETE_INFO",packet -> {
            CloudAPI.getInstance().getServerGroupManager().removeGroup(packet.convert(GroupDeleteInfo.class).getGroup());
        });
    }
}