package de.curse.allround.core.cloud.module;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.RedisURI;
import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet_types.module.ModuleDisconnectInfo;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Getter
@Setter
public class ModuleManager {
    protected final List<Module> modules;
    protected final Module thisModule;
    protected UUID mainNode;
    protected boolean mainNodeRegistered;

    @Contract(pure = true)
    public ModuleManager(Module module) {
        thisModule = module;
        this.modules = new CopyOnWriteArrayList<>();
    }

    public List<Module> getAllModules() {
        List<Module> modules = new ArrayList<>(this.modules);
        modules.add(thisModule);
        return modules;
    }

    public Module getThisModule() {
        return thisModule;
    }

    public boolean isMainNode() {
        return mainNode.equals(thisModule.getNetworkId());
    }

    public void setThisModule(String name, ModuleType moduleType) {
        addModule(new Module(moduleType, thisModule.getNetworkId(), name));
    }



    public boolean addModule(Module module) {
        if (modules.stream().anyMatch(module1 -> module1.getName().equals(module.getName()) && module1.getNetworkId().equals(module.getNetworkId())))
            return false;
        modules.add(module);
        return true;
    }

    public List<Module> getModules(String name) {
        return modules.stream().filter(module -> module.getName().equals(name)).collect(Collectors.toList());
    }

    public Optional<Module> getThisModule(UUID networkId) {
        return modules.stream().filter(module -> module.getNetworkId().equals(networkId)).findFirst();
    }


    public List<Module> getNodes() {
        return modules.stream().filter(module -> module.getModuleType().equals(ModuleType.NODE)).collect(Collectors.toList());
    }

    public List<Module> getServers() {
        return modules.stream().filter(module -> module.getModuleType().equals(ModuleType.SERVER)).collect(Collectors.toList());
    }

    public List<Module> getProxies() {
        return modules.stream().filter(module -> module.getModuleType().equals(ModuleType.PROXY)).collect(Collectors.toList());
    }

    public List<Module> getExtensions() {
        return modules.stream().filter(module -> module.getModuleType().equals(ModuleType.EXTENSION)).collect(Collectors.toList());
    }

    public void removeModules(String name) {
        modules.removeIf(module -> module.getName().equals(name));
    }

    public void removeModule(UUID networkId) {
        modules.removeIf(module -> module.getNetworkId().equals(networkId));
    }
}
