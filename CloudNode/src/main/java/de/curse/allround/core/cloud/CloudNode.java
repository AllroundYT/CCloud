package de.curse.allround.core.cloud;

import de.curse.allround.core.cloud.config.CloudConfiguration;
import de.curse.allround.core.cloud.extension.ExtensionManager;
import de.curse.allround.core.cloud.extension.NodeExtensionManager;
import de.curse.allround.core.cloud.module.Module;
import de.curse.allround.core.cloud.module.ModuleManager;
import de.curse.allround.core.cloud.module.ModuleType;
import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.player.PlayerManager;
import de.curse.allround.core.cloud.proxy.ProxyManager;
import de.curse.allround.core.cloud.server.ServerManager;
import de.curse.allround.core.cloud.servergroup.NodeGroupManager;
import de.curse.allround.core.cloud.servergroup.ServerGroupManager;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class CloudNode extends CloudAPI{

    @Contract(pure = true)
    public static CloudNode getInstance(){
        return CloudAPI.getInstance(CloudNode.class);
    }

    public final static Logger LOGGER = LoggerFactory.getLogger("CloudLogger");

    private final ModuleManager moduleManager;
    private final ExtensionManager extensionManager;
    private final ServerManager serverManager;
    private final ServerGroupManager serverGroupManager;
    private final ProxyManager proxyManager;
    private final PlayerManager playerManager;
    private final NetworkManager networkManager;
    private final CloudConfiguration cloudConfiguration;
    public CloudNode() {
        this.moduleManager = new ModuleManager(new Module(ModuleType.NODE, UUID.randomUUID(),"Node"));
        this.extensionManager = new NodeExtensionManager();
        this.serverManager = new ServerManager();
        this.serverGroupManager = new NodeGroupManager();
        this.proxyManager = new ProxyManager();
        this.playerManager = new PlayerManager();
        this.networkManager = new NetworkManager();
        this.cloudConfiguration = new CloudConfiguration();
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
    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    @Override
    public ProxyManager getProxyManager() {
        return proxyManager;
    }

    @Override
    public ServerGroupManager getServerGroupManager() {
        return serverGroupManager;
    }

    @Override
    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    @Override
    public CloudConfiguration getConfiguration() {
        return cloudConfiguration;
    }

    @Override
    public ExtensionManager getExtensionManager() {
        return extensionManager;
    }

    @Override
    public void init() {

        try {
            Files.createDirectories(Path.of("Extensions"));
            Files.createDirectories(Path.of("Storage","ServerGroups"));
            Files.createDirectories(Path.of("Storage","Templates","Servers"));
            Files.createDirectories(Path.of("Storage","Templates","Proxies"));
            Files.createDirectories(Path.of("Temp","Servers"));
            Files.createDirectories(Path.of("Temp","Proxies"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        getConfiguration().load(Path.of("config.json"));


    }

    @Override
    public void start() {
        Instant start = Instant.now();
        LOGGER.info("Welcome to CurseCloud v1.0.0-Beta.");
        LOGGER.info("LOGO HERE");
        LOGGER.info("CloudNode starting...");

        getModuleManager().start();
        getNetworkManager().start();
        ((NodeGroupManager) getServerGroupManager()).start();

        LOGGER.info("Loading and enabling CloudExtensions...");
        getExtensionManager().scanForExtensions();
        getExtensionManager().loadAll(extensionInfo -> LOGGER.info("Extension loaded: "+extensionInfo.extensionInfoFile().getName()+"-"+extensionInfo.extensionInfoFile().getVersion()));
        getExtensionManager().enableAll(extensionInfo -> LOGGER.info("Extension enabled: "+extensionInfo.extensionInfoFile().getName()+"-"+extensionInfo.extensionInfoFile().getVersion()));
        LOGGER.info("CloudExtensions loaded and enabled.");

        if (getModuleManager().isMainNode()){
            LOGGER.warn("This CloudNode has claimed the position as \"MainNode\".");
        }else {
            requestNeededData();
        }

        LOGGER.info("CloudNode started in "+ Duration.between(start,Instant.now()).toMillis() +" ms. Waiting for command...");
    }

    @Override
    public void stop() {
        Instant stop = Instant.now();
        LOGGER.info("Stopping CloudNode...");

        getExtensionManager().disableAll(extensionInfo -> LOGGER.info("Extension disabled: "+extensionInfo.extensionInfoFile().getName()+"-"+extensionInfo.extensionInfoFile().getVersion()));

        if (getModuleManager().isMainNode()){
            LOGGER.warn("As this CloudNode was the \"MainNode\" an other CloudNode claimed this job. If there is no other CloudNode the cloud will stop completely.");
        }

        getNetworkManager().stop();
        getModuleManager().stop();

        LOGGER.info("CloudNode stopped in "+Duration.between(stop,Instant.now()).toMillis()+" ms. Have a nice day...");
    }

    public void requestNeededData(){

    }
}