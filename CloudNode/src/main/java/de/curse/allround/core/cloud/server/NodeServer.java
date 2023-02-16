package de.curse.allround.core.cloud.server;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet.PacketConverter;
import de.curse.allround.core.cloud.network.packet_types.server.*;
import de.curse.allround.core.cloud.servergroup.ServerGroup;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class NodeServer extends Server {
    public NodeServer(String name, UUID node, UUID networkId, String status, ServerGroup serverGroup, boolean maintenance, String joinPermissions, String host, int port, boolean running) {
        super(name, node, networkId, status, serverGroup, maintenance, joinPermissions, host, port, running);
    }

    public NodeServer(String name,ServerGroup serverGroup) {
        super(name,serverGroup);
    }

    public void startProcess(){

    }

    @Override
    public CompletableFuture<Boolean> start(StartConfiguration startConfiguration) {
        //Wenn dieser server von diesem Node kontrolliert wird, ist er auf diesem vServer und muss von diesem Node gestartet werden.
        if (getNode().equals(CloudAPI.getInstance().getModuleManager().getThisModule().getNetworkId())){

            //hier muss der Process gestartet werden und dann ein update gesendet

            setStatus("STARTING");
            broadcastUpdate();
            return null;
        }

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
        ServerKickAllRequest serverKickAllRequest = new ServerKickAllRequest(getName(),reason);
        return NetworkManager.getInstance().sendRequest(serverKickAllRequest).handleAsync((packet, throwable) -> {
            ServerKickAllResponse serverKickAllResponse = PacketConverter.getInstance().convert(packet);
            return serverKickAllResponse.getResult().equalsIgnoreCase("SUCCESS");
        });
    }
}
