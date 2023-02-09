package de.curse.allround.core.cloud.network.packet;

import de.curse.allround.core.cloud.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Base64;
import java.util.UUID;

@Getter
@Setter
public class Packet {
    private final UUID sender = NetworkManager.getInstance().getIdentityManager().getThisIdentity();
    private final long timestamp = System.currentTimeMillis();
    private final String type;
    private final PacketChannel channel;
    private final String[] data;
    private UUID requestId;
    private UUID responseId;
    private UUID receiver;

    private Packet(String type, PacketChannel channel, String[] data) {
        this.type = type;
        this.channel = channel;
        this.data = data;
    }

    private Packet(UUID receiver, String type, PacketChannel channel, String[] data) {
        this.receiver = receiver;
        this.type = type;
        this.channel = channel;
        this.data = data;
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Packet regular(String type, PacketChannel channel, String[] data) {
        return new Packet(type, channel, data);
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull Packet request(UUID receiver, String type, PacketChannel channel, String[] data) {
        Packet packet = new Packet(receiver, type, channel, data);
        packet.setRequestId(UUID.randomUUID());
        return packet;
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull Packet response(UUID responseId, String type, PacketChannel channel, String[] data) {
        Packet packet = new Packet(type, channel, data);
        packet.setResponseId(responseId);
        return packet;
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull Packet addressed(UUID receiver, String type, PacketChannel channel, String[] data) {
        return new Packet(receiver, type, channel, data);
    }

    @Contract(pure = true)
    public static byte @Nullable [] serialize(Packet packet) {
        return Base64.getEncoder().encodeToString((JsonUtil.GSON.toJson(packet)).getBytes()).getBytes();
    }

    @Contract(pure = true)
    public static @Nullable Packet deserialize(byte[] bytes) {
        return JsonUtil.GSON.fromJson(new String(Base64.getDecoder().decode(bytes)), Packet.class);
    }

    public boolean isAddressed() {
        return receiver != null;
    }

    public boolean isRequest() {
        return requestId != null;
    }

    public boolean isResponse() {
        return responseId != null;
    }
}
