package de.curse.allround.core.cloud.network.packet;

import de.curse.allround.core.cloud.network.listener.DefaultListener;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class NetworkManager {

    private volatile static NetworkManager instance;

    public static NetworkManager getInstance() {
        return instance;
    }

    @Getter
    private final EventBus eventBus;
    @Getter(AccessLevel.PRIVATE)
    private final Publisher publisher;
    @Getter(AccessLevel.PRIVATE)
    private final Receiver receiver;

    @Contract(pure = true)
    public NetworkManager() {
        NetworkManager.instance = this;
        this.eventBus = new EventBus();
        this.publisher = new Publisher();
        this.receiver = new Receiver();
    }

    public boolean sendPacket(Packet packet){
        try {
            getPublisher().publish(packet);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendPacket(@NotNull PacketType packetType){
        return sendPacket(packetType.toPacket());
    }

    public CompletableFuture<Packet> sendRequest(Packet packet){
        sendPacket(packet);
        if (packet.getRequestId() == null) return null;
        CompletableFuture<Packet> future = new CompletableFuture<>();
        getEventBus().addRequestFuture(packet.getRequestId(),future);
        return future;
    }

    public CompletableFuture<Packet> sendRequest(@NotNull PacketType packetType){
        return sendRequest(packetType.toPacket());
    }

    public void start() {
        try {
            receiver.init();
            publisher.init();
            new DefaultListener().register();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            receiver.stop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
