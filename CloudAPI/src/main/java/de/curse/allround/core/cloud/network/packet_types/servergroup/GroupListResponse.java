package de.curse.allround.core.cloud.network.packet_types.servergroup;

import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import de.curse.allround.core.cloud.util.JsonUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class GroupListResponse extends PacketType {
    private final String[] groups;
    private final UUID responseId;
    public GroupListResponse(Packet packet) {
        super(packet);
        this.responseId = packet.getResponseId();
        groups = JsonUtil.GSON.fromJson(packet.getData()[0],String[].class);
    }

    @Override
    public Packet toPacket() {
        return Packet.response(responseId,"GROUP_LIST_RESPONSE",JsonUtil.GSON.toJson(groups));
    }
}
