package de.curse.allround.core.cloud.proxy;

import lombok.Data;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class ProxyManager {
    private final List<Proxy> proxies;

    @Contract(pure = true)
    public ProxyManager() {
        this.proxies = new CopyOnWriteArrayList<>();
    }

    public Optional<Proxy> getProxy(String name){
        return proxies.stream().filter(proxy -> proxy.getName().equals(name)).findFirst();
    }

    public boolean addProxy(Proxy proxy){
        if (proxies.stream().anyMatch(proxy1 -> proxy1.getName().equals(proxy.getName()))) return false;
        proxies.add(proxy);
        return true;
    }

    public void removeProxy(String name){
        proxies.removeIf(proxy -> proxy.getName().equals(name));
    }
}
