package de.curse.allround.core.cloud.network.listener.group;

import de.curse.allround.core.cloud.CloudNode;
import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet.PacketBus;
import de.curse.allround.core.cloud.network.packet_types.group.GroupTemplateRequest;
import de.curse.allround.core.cloud.network.packet_types.group.GroupTemplateResponse;
import de.curse.allround.core.cloud.network.packet_types.group.GroupTemplateUpdateInfo;

import java.nio.file.Path;

public class GroupListener {
    public void register(){
        PacketBus packetBus = NetworkManager.getInstance().getPacketBus();

        packetBus.listen("GROUP_TEMPLATE_REQUEST",packet -> {
            GroupTemplateRequest groupTemplateRequest = packet.convert(GroupTemplateRequest.class);
            GroupTemplateResponse templateResponse = new GroupTemplateResponse(
                    packet.getRequestId(),
                    Path.of("Storage","Templates","Servers", groupTemplateRequest.getGroup()).toFile(),
                    groupTemplateRequest.getGroup()
            );
            NetworkManager.getInstance().sendPacket(templateResponse);
        });

        packetBus.listen("GROUP_TEMPLATE_UPDATE_INFO",packet -> {
            GroupTemplateUpdateInfo updateInfo = packet.convert(GroupTemplateUpdateInfo.class);
            CloudNode.getInstance().getServerGroupManager().onTemplateUpdateInfo(updateInfo);
        });
    }
}
