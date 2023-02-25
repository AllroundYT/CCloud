package de.curse.allround.core.cloud.network.packet;


import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.listener.PacketListener;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter(AccessLevel.PROTECTED)
public class PacketBus {

    private final List<SimpleListener> simpleListeners;
    private final Map<UUID, CompletableFuture<Packet>> requestFutures;

    public PacketBus() {
        this.requestFutures = new HashMap<>();
        this.simpleListeners = new CopyOnWriteArrayList<>();
    }

    public CompletableFuture<Packet> getRequestFuture(UUID requestId){
        return requestFutures.get(requestId);
    }

    public void addRequestFuture(UUID requestId,CompletableFuture<Packet> future){
        requestFutures.put(requestId,future);
    }

    public void removeRequestFuture(UUID requestId){
        requestFutures.remove(requestId);
    }

    public void unregister(UUID uuid) {
        this.simpleListeners.removeIf(packetListener -> packetListener.getUuid().equals(uuid));
    }

    public void onPacket(@NotNull Packet packet) {
        if (packet.getResponseId() != null){
            CompletableFuture<Packet> future = getRequestFuture(packet.getResponseId());
            if (future != null){
                future.completeAsync(() -> packet);
                removeRequestFuture(packet.getResponseId());
            }
        }


        simpleListeners.forEach(listener -> {
            if (packet.isAddressed() && !CloudAPI.getInstance().getModuleManager().getThisModule().getNetworkId().equals(packet.getReceiver()))
                return;
            if (listener.getResponseId() != null && packet.isResponse() && !listener.getResponseId().equals(packet.getResponseId()))
                return;

            if (!listener.getType().equals(packet.getType())) return;

            listener.getPacketListener().listen(packet);
        });
    }

    public UUID listen(String type, PacketListener packetListener) {
        UUID listenerId = UUID.randomUUID();
        SimpleListener simpleListener = new SimpleListener(type,  listenerId, packetListener);
        this.simpleListeners.add(simpleListener);
        return listenerId;
    }

    public UUID listenResponse(String type,  UUID responseId, PacketListener packetListener) {
        UUID listenerId = UUID.randomUUID();
        SimpleListener simpleListener = new SimpleListener(type,  listenerId, packetListener);
        simpleListener.setResponseId(responseId);
        this.simpleListeners.add(simpleListener);
        return listenerId;
    }


    @Data
    private static class SimpleListener {
        private final String type;
        private final UUID uuid;
        private final PacketListener packetListener;

        private UUID responseId;
    }
}
