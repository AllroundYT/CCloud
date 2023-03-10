package de.curse.allround.core.cloud.proxy;

import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet_types.proxy.ProxyDeleteInfo;
import lombok.Data;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public abstract class ProxyManager {
    private final List<Proxy> proxies;
    private final Class<? extends Proxy> proxyImplClass;

    @Contract(pure = true)
    public ProxyManager(Class<? extends Proxy> proxyImplClass) {
        this.proxyImplClass = proxyImplClass;
        this.proxies = new CopyOnWriteArrayList<>();
    }

    public abstract Proxy createProxy();
    public synchronized void deleteProxy(String proxy){
        if (getProxy(proxy).isEmpty()) return;
        if (getProxy(proxy).get().isRunning()){
            getProxy(proxy).get().stop().handleAsync((success, throwable) -> {
                if ((boolean)success){
                    ProxyDeleteInfo proxyDeleteInfo = new ProxyDeleteInfo(proxy);
                    NetworkManager.getInstance().sendPacket(proxyDeleteInfo);
                    removeProxy(proxy);
                }
                return success;
            });
        }else {
            ProxyDeleteInfo proxyDeleteInfo = new ProxyDeleteInfo(proxy);
            NetworkManager.getInstance().sendPacket(proxyDeleteInfo);
            removeProxy(proxy);
        }
    }

    public synchronized Optional<Proxy> getProxy(String name){
        return proxies.stream().filter(proxy -> proxy.getName().equals(name)).findFirst();
    }

    public synchronized boolean addProxy(Proxy proxy){
        if (proxies.stream().anyMatch(proxy1 -> proxy1.getName().equals(proxy.getName()))) return false;
        proxies.add(proxy);
        return true;
    }

    public synchronized void removeProxy(String name){
        proxies.removeIf(proxy -> proxy.getName().equals(name));
    }
}
