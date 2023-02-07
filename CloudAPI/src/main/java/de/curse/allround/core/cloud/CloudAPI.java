package de.curse.allround.core.cloud;

import de.curse.allround.core.beta.NetworkAPI;
import de.curse.allround.core.beta.network.PacketChannel;
import de.curse.allround.core.cloud.module.ModuleManager;
import de.curse.allround.core.cloud.network.listener.ModuleConnectListener;
import de.curse.allround.core.cloud.player.PlayerManager;
import de.curse.allround.core.cloud.proxy.ProxyManager;
import de.curse.allround.core.cloud.server.ServerManager;
import de.curse.allround.core.cloud.servergroup.ServerGroupManager;
import de.curse.allround.core.util.Initializeable;
import de.curse.allround.core.util.Startable;
import de.curse.allround.core.util.Stopable;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Basis Klasse
 */
@Getter
public abstract class CloudAPI implements Startable, Stopable, Initializeable {
    private static CloudAPI instance;

    private final ModuleManager moduleManager;
    private final PlayerManager playerManager;
    private final ProxyManager proxyManager;
    private final ServerManager serverManager;
    private final ServerGroupManager groupManager;

    @Contract(pure = true)
    public CloudAPI() {
        if (CloudAPI.instance != null) {
            throw new RuntimeException("CloudAPI can only be initialized once.");
        }
        CloudAPI.instance = this;
        this.moduleManager = new ModuleManager();
        this.playerManager = new PlayerManager();
        this.proxyManager = new ProxyManager();
        this.serverManager = new ServerManager();
        this.groupManager = new ServerGroupManager();
    }

    public void registerDefaultNetworkListener(){
        NetworkAPI.getInstance().getEventBus().listen("MODULE_CONNECTED_INFO", PacketChannel.CLOUD,new ModuleConnectListener());
    }


    @Contract(pure = true)
    public static CloudAPI getInstance() {
        return instance;
    }

    @Contract(pure = true)
    public static <T extends CloudAPI> T getInstance(@NotNull Class<T> tClass){
        return tClass.cast(instance);
    }
}
