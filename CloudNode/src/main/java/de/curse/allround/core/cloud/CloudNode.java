package de.curse.allround.core.cloud;

import de.curse.allround.core.cloud.cli.CommandManager;
import de.curse.allround.core.cloud.config.NodeConfiguration;
import de.curse.allround.core.cloud.extension.NodeExtensionManager;
import de.curse.allround.core.cloud.module.Module;
import de.curse.allround.core.cloud.module.ModuleType;
import de.curse.allround.core.cloud.module.NodeModuleManager;
import de.curse.allround.core.cloud.network.NodeNetworkManager;
import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet.PacketConverter;
import de.curse.allround.core.cloud.network.packet_types.player.PlayerDataRequest;
import de.curse.allround.core.cloud.network.packet_types.player.PlayerDataResponse;
import de.curse.allround.core.cloud.network.packet_types.player.PlayerListRequest;
import de.curse.allround.core.cloud.network.packet_types.player.PlayerListResponse;
import de.curse.allround.core.cloud.network.packet_types.proxy.ProxyDataRequest;
import de.curse.allround.core.cloud.network.packet_types.proxy.ProxyDataResponse;
import de.curse.allround.core.cloud.network.packet_types.proxy.ProxyListRequest;
import de.curse.allround.core.cloud.network.packet_types.proxy.ProxyListResponse;
import de.curse.allround.core.cloud.network.packet_types.server.ServerDataRequest;
import de.curse.allround.core.cloud.network.packet_types.server.ServerDataResponse;
import de.curse.allround.core.cloud.network.packet_types.server.ServerListRequest;
import de.curse.allround.core.cloud.network.packet_types.server.ServerListResponse;
import de.curse.allround.core.cloud.network.packet_types.servergroup.GroupDataRequest;
import de.curse.allround.core.cloud.network.packet_types.servergroup.GroupDataResponse;
import de.curse.allround.core.cloud.network.packet_types.servergroup.GroupListRequest;
import de.curse.allround.core.cloud.network.packet_types.servergroup.GroupListResponse;
import de.curse.allround.core.cloud.player.NodePlayer;
import de.curse.allround.core.cloud.player.PlayerManager;
import de.curse.allround.core.cloud.proxy.NodeProxy;
import de.curse.allround.core.cloud.proxy.NodeProxyManager;
import de.curse.allround.core.cloud.proxy.ProxyManager;
import de.curse.allround.core.cloud.server.NodeServer;
import de.curse.allround.core.cloud.server.ServerManager;
import de.curse.allround.core.cloud.servergroup.NodeGroupManager;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CloudNode extends CloudAPI {

    public final static Logger LOGGER = LoggerFactory.getLogger(CloudNode.class);
    private final NodeModuleManager moduleManager;
    private final NodeExtensionManager extensionManager;
    private final ServerManager serverManager;
    private final NodeGroupManager serverGroupManager;
    private final ProxyManager proxyManager;
    private final PlayerManager playerManager;
    private final NodeNetworkManager networkManager;
    private final NodeConfiguration nodeConfiguration;
    private final CommandManager commandManager;

    public CloudNode() {

        this.nodeConfiguration = new NodeConfiguration();
        getConfiguration().load(Path.of("config.json"));

        this.moduleManager = new NodeModuleManager(new Module(ModuleType.NODE, UUID.randomUUID(), "Node"));
        this.extensionManager = new NodeExtensionManager();
        this.serverManager = new ServerManager(NodeServer.class);
        this.serverGroupManager = new NodeGroupManager();
        this.proxyManager = new NodeProxyManager(NodeProxy.class);
        this.playerManager = new PlayerManager(NodePlayer.class);
        this.networkManager = new NodeNetworkManager();
        this.commandManager = new CommandManager();
    }

    @Contract(pure = true)
    public static CloudNode getInstance() {
        return CloudAPI.getInstance(CloudNode.class);
    }

    @Override
    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    @Override
    public ServerManager getServerManager() {
        return serverManager;
    }

    @Override
    public NodeNetworkManager getNetworkManager() {
        return networkManager;
    }

    @Override
    public ProxyManager getProxyManager() {
        return proxyManager;
    }

    @Override
    public NodeGroupManager getServerGroupManager() {
        return serverGroupManager;
    }

    @Override
    public NodeModuleManager getModuleManager() {
        return moduleManager;
    }


    public NodeConfiguration getConfiguration() {
        return nodeConfiguration;
    }

    @Override
    public NodeExtensionManager getExtensionManager() {
        return extensionManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(Path.of("Extensions"));
            Files.createDirectories(Path.of("Storage", "ServerGroups"));
            Files.createDirectories(Path.of("Storage", "Templates", "Servers"));
            Files.createDirectories(Path.of("Storage", "Templates", "Proxies"));
            Files.createDirectories(Path.of("Temp", "Servers"));
            Files.createDirectories(Path.of("Temp", "Proxies"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        Instant start = Instant.now();
        System.out.println("Welcome to CurseCloud v1.0.0-Beta.");
        System.out.println("LOGO HERE");
        System.out.println("CloudNode starting...");

        getCommandManager().start();
        getModuleManager().start();
        getNetworkManager().start();
        getServerGroupManager().start();

        System.out.println("Loading and enabling CloudExtensions...");
        getExtensionManager().scanForExtensions();
        getExtensionManager().loadAll(extensionInfo -> System.out.println("Extension loaded: " + extensionInfo.extensionInfoFile().getName() + "-" + extensionInfo.extensionInfoFile().getVersion()));
        getExtensionManager().enableAll(extensionInfo -> System.out.println("Extension enabled: " + extensionInfo.extensionInfoFile().getName() + "-" + extensionInfo.extensionInfoFile().getVersion()));
        System.out.println("CloudExtensions loaded and enabled.");

        if (getModuleManager().isMainNode()) {
            System.out.println("This CloudNode has claimed the position as \"MainNode\".");
        } else {
            System.out.println("Downloading data from main node...");
            if (!requestNeededData()) {
                System.out.println("Could not properly download data.");
                stop();
            }else {
                System.out.println("Data download done.");
            }
        }

        System.out.println("CloudNode started in " + Duration.between(start, Instant.now()).toMillis() + " ms. Waiting for command...");
    }

    @Override
    public void stop() {
        Instant stop = Instant.now();
        System.out.println("Stopping CloudNode...");

        getExtensionManager().disableAll(extensionInfo -> System.out.println("Extension disabled: " + extensionInfo.extensionInfoFile().getName() + "-" + extensionInfo.extensionInfoFile().getVersion()));

        if (getModuleManager().isMainNode()) {
            System.err.println("As this CloudNode was the \"MainNode\" an other CloudNode claimed this job. If there is no other CloudNode the cloud will stop completely.");
        }

        getNetworkManager().stop();
        getModuleManager().stop();
        getCommandManager().stop();

        System.out.println("CloudNode stopped in " + Duration.between(stop, Instant.now()).toMillis() + " ms. Have a nice day...");
    }

    public boolean requestNeededData() {

        try {
            //REQUEST PROXY DATA ASYNC
            return NetworkManager.getInstance().sendRequest(new ProxyListRequest()).handleAsync((packet, throwable) -> {
                if (throwable != null) {
                    throwable.printStackTrace();
                    return false;
                }
                ProxyListResponse proxyListResponse = PacketConverter.getInstance().convert(packet);
                for (String proxy : proxyListResponse.getProxies()) {
                    NetworkManager.getInstance().sendRequest(new ProxyDataRequest(proxy)).handleAsync((packet1, throwable1) -> {
                        if (throwable1 != null) {
                            throwable1.printStackTrace();
                            return false;
                        }
                        ProxyDataResponse proxyDataResponse = PacketConverter.getInstance().convert(packet1);
                        CloudAPI.getInstance().getProxyManager().addProxy(proxyDataResponse.getProxy());
                        return true;
                    });
                }
                return true;
            }).thenRun(() -> {
                //REQUEST GROUP DATA ASYNC
                NetworkManager.getInstance().sendRequest(new GroupListRequest()).handleAsync((packet, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                        return false;
                    }
                    GroupListResponse groupListResponse = PacketConverter.getInstance().convert(packet);
                    for (String group : groupListResponse.getGroups()) {
                        NetworkManager.getInstance().sendRequest(new GroupDataRequest(group)).handleAsync((packet1, throwable1) -> {
                            if (throwable1 != null) {
                                throwable1.printStackTrace();
                                return false;
                            }
                            GroupDataResponse groupDataResponse = PacketConverter.getInstance().convert(packet1);
                            CloudAPI.getInstance().getServerGroupManager().addGroup(groupDataResponse.getServerGroup());
                            return true;
                        });
                    }

                    return true;
                });
            }).thenRun(() -> {
                //REQUEST SERVER DATA ASYNC
                NetworkManager.getInstance().sendRequest(new ServerListRequest()).handleAsync((packet, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                        return false;
                    }
                    ServerListResponse serverListResponse = PacketConverter.getInstance().convert(packet);
                    for (String server : serverListResponse.getServers()) {
                        NetworkManager.getInstance().sendRequest(new ServerDataRequest(server)).handleAsync((packet1, throwable1) -> {
                            if (throwable1 != null) {
                                throwable1.printStackTrace();
                                return false;
                            }
                            ServerDataResponse serverDataResponse = PacketConverter.getInstance().convert(packet1);
                            CloudAPI.getInstance().getServerManager().addServer(serverDataResponse.getServer());
                            return true;
                        });
                    }
                    return true;
                });
            }).thenRun(() -> {
                //REQUEST PLAYER DATA ASYNC
                NetworkManager.getInstance().sendRequest(new PlayerListRequest()).handleAsync((packet, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                        return false;
                    }
                    PlayerListResponse playerListResponse = PacketConverter.getInstance().convert(packet);
                    for (UUID uuid : playerListResponse.getPlayers()) {
                        NetworkManager.getInstance().sendRequest(new PlayerDataRequest(uuid)).handleAsync((packet1, throwable1) -> {
                            if (throwable1 != null) {
                                throwable1.printStackTrace();
                                return false;
                            }
                            PlayerDataResponse playerDataResponse = PacketConverter.getInstance().convert(packet1);
                            CloudAPI.getInstance().getPlayerManager().addPlayer(playerDataResponse.getPlayer());
                            return true;
                        });
                    }
                    return true;
                });
            }).handle((unused, throwable) -> {
                if (throwable != null) {
                    throwable.printStackTrace();
                    return false;
                }
                return true;
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }
}