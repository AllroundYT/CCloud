package de.curse.allround.core.cloud;


import de.curse.allround.core.cloud.extension.ExtensionManager;
import de.curse.allround.core.cloud.module.ModuleManager;
import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.player.PlayerManager;
import de.curse.allround.core.cloud.proxy.ProxyManager;
import de.curse.allround.core.cloud.server.ServerManager;
import de.curse.allround.core.cloud.servergroup.ServerGroupManager;
import de.curse.allround.core.cloud.util.Initializeable;
import de.curse.allround.core.cloud.util.Startable;
import de.curse.allround.core.cloud.util.Stopable;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Basis Klasse
 */
@Getter
public abstract class CloudAPI implements Startable, Stopable, Initializeable {
    private static CloudAPI instance;

    @Contract(pure = true)
    public CloudAPI() {
        if (CloudAPI.instance != null) {
            throw new RuntimeException("CloudAPI can only be initialized once.");
        }
        CloudAPI.instance = this;
    }

    public abstract PlayerManager getPlayerManager();

    public abstract ServerManager getServerManager();

    public abstract NetworkManager getNetworkManager();

    public abstract ProxyManager getProxyManager();

    public abstract ServerGroupManager getServerGroupManager();

    public abstract ModuleManager getModuleManager();

    @Override
    public abstract void init();

    public abstract ExtensionManager getExtensionManager();

    @Contract(pure = true)
    public static CloudAPI getInstance() {
        return instance;
    }

    @Contract(pure = true)
    public static <T extends CloudAPI> T getInstance(@NotNull Class<T> tClass){
        return tClass.cast(instance);
    }
}
