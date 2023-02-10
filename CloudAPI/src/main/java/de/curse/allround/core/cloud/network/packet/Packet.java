package de.curse.allround.core.cloud.network.packet;

import de.curse.allround.core.cloud.CloudAPI;
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
    private final UUID sender = CloudAPI.getInstance().getModuleManager().getThisModule().getNetworkId();
    private final long timestamp = System.currentTimeMillis();
    private final String type;
    private final String[] data;
    private UUID requestId;
    private UUID responseId;
    private UUID receiver;

    private Packet(String type, String[] data) {
        this.type = type;
        this.data = data;
    }

    private Packet(UUID receiver, String type, String[] data) {
        this.receiver = receiver;
        this.type = type;
        this.data = data;
    }

    @Contract("_, _ -> new")
    public static @NotNull Packet regular(String type,  String... data) {
        return new Packet(type,  data);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Packet request(UUID receiver, String type,  String... data) {
        Packet packet = new Packet(receiver, type, data);
        packet.setRequestId(UUID.randomUUID());
        return packet;
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Packet response(UUID responseId, String type,  String... data) {
        Packet packet = new Packet(type,  data);
        packet.setResponseId(responseId);
        return packet;
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Packet addressed(UUID receiver, String type,  String... data) {
        return new Packet(receiver, type,  data);
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
