package de.curse.allround.core.cloud.module;

import de.curse.allround.core.beta.NetworkAPI;
import lombok.Getter;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Getter
public class ModuleManager {
    private final List<Module> modules;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    //TODO
    private final String redisHost = "localhost";
    private final int redisPort = 5555;
    private UUID mainNode;
    private boolean mainNodeRegistered;

    public Optional<Module> getThisModule(){
        return getModule(NetworkAPI.getInstance().getIdentityManager().getThisIdentity());
    }

    public boolean isMainNode(){
        return mainNode.equals(NetworkAPI.getInstance().getIdentityManager().getThisIdentity());
    }

    public void setThisModule(String name,ModuleType moduleType){
        addModule(new Module(moduleType,NetworkAPI.getInstance().getIdentityManager().getThisIdentity(),name));
    }

    @Contract(pure = true)
    public ModuleManager() {
        this.modules = new CopyOnWriteArrayList<>();
    }

    public void init() {
        //TODO: verbindung zu redis aufbauen
    }

    public void start() {
        init();
        executorService.scheduleWithFixedDelay(this::update, 0, 1, TimeUnit.MINUTES);
    }

    public void stop() {
        executorService.shutdownNow();
        //TODO:wenn mainnode dann austragen und danach redis connection schließen
    }

    public void update() {
        //TODO: checken ob main node eingetragen ist. Wenn 2mal nicht dann eigenen Node eintragen
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

    public Optional<Module> getModule(UUID networkId) {
        return modules.stream().filter(module -> module.getNetworkId().equals(networkId)).findFirst();
    }

    public List<Module> getDaemons() {
        return modules.stream().filter(module -> module.getModuleType().equals(ModuleType.DAEMON)).collect(Collectors.toList());
    }

    public Optional<Module> getController() {
        return modules.stream().filter(module -> module.getModuleType().equals(ModuleType.CONTROLLER)).findFirst();
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
