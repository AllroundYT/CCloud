package de.curse.allround.core.cloud.network.packet;


import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.listener.PacketListener;
import de.curse.allround.core.cloud.network.packet.listener.SimplePacketListener;
import de.curse.allround.core.cloud.network.packet.listener.SpecificTypePacketListener;
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
public class EventBus {

    private final List<Listener> listeners;
    private final List<SpecificTypeListener> specificTypeListeners;
    private final List<SimpleListener> simpleListeners;
    private final Map<UUID, CompletableFuture<Packet>> requestFutures;

    public EventBus() {
        this.requestFutures = new HashMap<>();
        this.simpleListeners = new CopyOnWriteArrayList<>();
        this.listeners = new CopyOnWriteArrayList<>();
        this.specificTypeListeners = new CopyOnWriteArrayList<>();
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
        this.listeners.removeIf(packetListener -> packetListener.getUuid().equals(uuid));
        this.specificTypeListeners.removeIf(specificTypeListener -> specificTypeListener.getUuid().equals(uuid));
    }

    public void onPacket(@NotNull Packet packet) {
        if (packet.getResponseId() != null){
            CompletableFuture<Packet> future = getRequestFuture(packet.getResponseId());
            if (future != null){
                future.completeAsync(() -> packet);
                removeRequestFuture(packet.getResponseId());
            }
        }

        listeners.forEach(listener -> {
            if (packet.isAddressed() && !CloudAPI.getInstance().getModuleManager().getThisModule().getNetworkId().equals(packet.getReceiver()))
                return;
            if (listener.getResponseId() != null && packet.isResponse() && !listener.getResponseId().equals(packet.getResponseId()))
                return;

            listener.packetListener.listen(packet.getType(), packet);
        });

        simpleListeners.forEach(listener -> {
            if (packet.isAddressed() && !CloudAPI.getInstance().getModuleManager().getThisModule().getNetworkId().equals(packet.getReceiver()))
                return;
            if (listener.getResponseId() != null && packet.isResponse() && !listener.getResponseId().equals(packet.getResponseId()))
                return;

            if (!listener.getType().equals(packet.getType())) return;

            listener.getSimplePacketListener().listen(packet);
        });

        specificTypeListeners.forEach(listener -> {
            if (packet.isAddressed() && !CloudAPI.getInstance().getModuleManager().getThisModule().getNetworkId().equals(packet.getReceiver()))
                return;
            if (!PacketConverter.getInstance().isConvertable(packet)) return;
            if (listener.getResponseId() != null && packet.isResponse() && !listener.getResponseId().equals(packet.getResponseId()))
                return;

            if (!listener.getType().equals(packet.getType())) return;

            listener.getSpecificTypePacketListener().listen(PacketConverter.getInstance().convert(packet));
        });
    }

    public UUID listen(String type, SimplePacketListener simplePacketListener) {
        UUID listenerId = UUID.randomUUID();
        SimpleListener simpleListener = new SimpleListener(type,  listenerId, simplePacketListener);
        this.simpleListeners.add(simpleListener);
        return listenerId;
    }

    public UUID listen(String type, SpecificTypePacketListener<?> specificTypePacketListener) {
        UUID listenerId = UUID.randomUUID();
        SpecificTypeListener specificTypeListener = new SpecificTypeListener(type,  listenerId, specificTypePacketListener);
        specificTypeListeners.add(specificTypeListener);
        return listenerId;
    }

    public UUID listen(PacketListener packetListener) {
        UUID listenerId = UUID.randomUUID();
        Listener listener = new Listener(listenerId, packetListener);
        this.listeners.add(listener);
        return listenerId;
    }

    public UUID listenResponse(UUID responseId, PacketListener packetListener) {
        UUID listenerId = UUID.randomUUID();
        Listener listener = new Listener(listenerId, packetListener);
        listener.setResponseId(responseId);
        this.listeners.add(listener);
        return listenerId;
    }

    public UUID listenResponse(String type,  UUID responseId, SimplePacketListener simplePacketListener) {
        UUID listenerId = UUID.randomUUID();
        SimpleListener simpleListener = new SimpleListener(type,  listenerId, simplePacketListener);
        simpleListener.setResponseId(responseId);
        this.simpleListeners.add(simpleListener);
        return listenerId;
    }

    public UUID listenResponse(String type,  UUID responseId, SpecificTypePacketListener<?> specificTypePacketListener) {
        UUID listenerId = UUID.randomUUID();
        SpecificTypeListener specificTypeListener = new SpecificTypeListener(type,  listenerId, specificTypePacketListener);
        specificTypeListener.setResponseId(responseId);
        specificTypeListeners.add(specificTypeListener);
        return listenerId;
    }

    public UUID listenResponse(PacketListener packetListener) {
        UUID listenerId = UUID.randomUUID();
        Listener listener = new Listener(listenerId, packetListener);
        this.listeners.add(listener);
        return listenerId;
    }

    @Data
    private static class Listener {
        private final UUID uuid;
        private final PacketListener packetListener;
        private UUID responseId;
    }

    @Data
    private static class SimpleListener {
        private final String type;
        private final UUID uuid;
        private final SimplePacketListener simplePacketListener;

        private UUID responseId;
    }

    @Data
    private static class SpecificTypeListener {
        private final String type;
        private final UUID uuid;
        private final SpecificTypePacketListener<?> specificTypePacketListener;
        private UUID responseId;
    }
}
