package de.curse.allround.core.cloud.network.packet_types.servergroup;

import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import de.curse.allround.core.cloud.server.StartConfiguration;
import de.curse.allround.core.cloud.servergroup.ServerGroup;
import de.curse.allround.core.cloud.servergroup.ServerGroupSnapshot;
import de.curse.allround.core.cloud.util.JsonUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Getter
public class GroupUpdateInfo extends PacketType {
    private final ServerGroup serverGroup;

    public GroupUpdateInfo(Packet packet) {
        super(packet);
        String name = packet.getData()[0];
        int minServers = Integer.parseInt(packet.getData()[1]);
        int maxServers = Integer.parseInt(packet.getData()[2]);
        Set<String> ignoredState = new HashSet<>(Arrays.asList(JsonUtil.GSON.fromJson(packet.getData()[3], String[].class)));
        StartConfiguration defaultStartConfiguration = JsonUtil.GSON.fromJson(packet.getData()[4],StartConfiguration.class);

        serverGroup = new ServerGroupSnapshot(name,minServers,maxServers,ignoredState,defaultStartConfiguration);
    }

    @Override
    public Packet toPacket() {
        return Packet.regular("GROUP_UPDATE_INFO",
                serverGroup.getName(),
                String.valueOf(serverGroup.getMinServers()),
                String.valueOf(serverGroup.getMaxServers()),
                JsonUtil.GSON.toJson(serverGroup.getIgnoredStates().toArray(new String[0])),
                JsonUtil.GSON.toJson(serverGroup.getDefaultStartConfiguration())
        );
    }
}
