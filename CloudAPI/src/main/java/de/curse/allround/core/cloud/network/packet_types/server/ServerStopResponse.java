package de.curse.allround.core.cloud.network.packet_types.server;

import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ServerStopResponse extends PacketType {
    private final UUID responseId;
    private final String result;
    public ServerStopResponse(Packet packet) {
        super(packet);
        this.responseId = packet.getResponseId();
        this.result = packet.getData()[0];
    }

    @Override
    public Packet toPacket() {
        return Packet.response(responseId,"SERVER_STOP_RESPONSE",result);
    }
}
