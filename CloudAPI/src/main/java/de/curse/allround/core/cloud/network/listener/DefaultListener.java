package de.curse.allround.core.cloud.network.listener;

import de.curse.allround.core.cloud.network.packet.EventBus;
import de.curse.allround.core.cloud.network.packet.NetworkManager;

/**
 * Registriert alle Listener die entweder Daten abfragen oder verändern. <br>
 * Start/Stop und ähnliche Requests werden jedoch nicht bearbeitet.
 */
public class DefaultListener {
    public void register(){
        EventBus eventBus = NetworkManager.getInstance().getEventBus();

        new ServerListener().register();
        new ProxyListener().register();
        new ModuleListener().register();
        new PlayerListener().register();
        new GroupListener().register();
    }
}
