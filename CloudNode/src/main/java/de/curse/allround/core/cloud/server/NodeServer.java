package de.curse.allround.core.cloud.server;

import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet.PacketConverter;
import de.curse.allround.core.cloud.network.packet_types.server.ServerStartRequest;
import de.curse.allround.core.cloud.network.packet_types.server.ServerStartResponse;
import de.curse.allround.core.cloud.network.packet_types.server.ServerStopRequest;
import de.curse.allround.core.cloud.network.packet_types.server.ServerStopResponse;
import de.curse.allround.core.cloud.servergroup.ServerGroup;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class NodeServer extends Server {
    public NodeServer(String name, UUID node, UUID networkId, String status, ServerGroup serverGroup, boolean maintenance, String joinPermissions, String host, int port, boolean running) {
        super(name, node, networkId, status, serverGroup, maintenance, joinPermissions, host, port, running);
    }

    @Override
    public CompletableFuture<Boolean> start(StartConfiguration startConfiguration) {
        ServerStartRequest serverStartRequest = new ServerStartRequest(getName());
        return NetworkManager.getInstance().sendRequest(serverStartRequest).handleAsync((packet, throwable) -> {
            ServerStartResponse serverStartResponse = PacketConverter.getInstance().convert(packet);
            if (serverStartResponse.getResult().equalsIgnoreCase("SUCCESS")) {
                setRunning(true);
                return true;
            }
            return false;
        });
    }

    @Override
    public CompletableFuture<Boolean> stop() {
        ServerStopRequest serverStopRequest = new ServerStopRequest(getName());
        return NetworkManager.getInstance().sendRequest(serverStopRequest).handleAsync((packet, throwable) -> {
            ServerStopResponse serverStopResponse = PacketConverter.getInstance().convert(packet);
            if (serverStopResponse.getResult().equalsIgnoreCase("SUCCESS")){
                setRunning(false);
                return true;
            }
            return false;
        });
    }

    @Override
    public CompletableFuture<Boolean> kickAll(String reason) {
        //TODO: send ServerKickAllRequest
        return null;
    }
}
