package de.curse.allround.core.cloud.module;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.RedisURI;
import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet_types.module.ModuleDisconnectInfo;
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
    private RedisClient redisClient;
    private RedisConnection<String,String> connection;
    private final String redisHost = "localhost";
    private final int redisPort = 5555;
    private UUID mainNode;
    private boolean mainNodeRegistered;

    public Optional<Module> getThisModule(){
        return getModule(NetworkManager.getInstance().getIdentityManager().getThisIdentity());
    }

    public boolean isMainNode(){
        return mainNode.equals(NetworkManager.getInstance().getIdentityManager().getThisIdentity());
    }

    public void setThisModule(String name,ModuleType moduleType){
        addModule(new Module(moduleType,NetworkManager.getInstance().getIdentityManager().getThisIdentity(),name));
    }

    @Contract(pure = true)
    public ModuleManager() {
        this.modules = new CopyOnWriteArrayList<>();
    }

    private void init() {
        redisClient = new RedisClient(RedisURI.create("redis://password@host:port"));
        connection = redisClient.connect();
    }

    public void start() {
        init();
        executorService.scheduleWithFixedDelay(this::update, 0, 1, TimeUnit.MINUTES);
    }

    public void stop() {
        executorService.shutdownNow();

        ModuleDisconnectInfo disconnectInfo = new ModuleDisconnectInfo(getThisModule().get());
        NetworkManager.getInstance().sendPacket(disconnectInfo);

        connection.close();
        redisClient.shutdown();
    }

    public void update() {
        String mainNode = connection.get("main-node-network-id");

        if (isMainNode()){
            connection.setex("main-node-network-id",10000,NetworkManager.getInstance().getIdentityManager().getThisIdentity().toString());
        } else if (mainNode == null) {
            if (!mainNodeRegistered){
                connection.set("main-node-network-id",NetworkManager.getInstance().getIdentityManager().getThisIdentity().toString());

                this.mainNode = NetworkManager.getInstance().getIdentityManager().getThisIdentity();

                mainNodeRegistered = true;
            }
            mainNodeRegistered = false;
        }else {
            this.mainNode = UUID.fromString(mainNode);
            mainNodeRegistered = true;
        }
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
