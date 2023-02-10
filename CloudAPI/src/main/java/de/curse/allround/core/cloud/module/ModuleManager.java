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
    private final List<Module> modules;
    private final Module thisModule;
    private final ScheduledExecutorService executorService;
    private RedisClient redisClient;
    private RedisConnection<String, String> connection;
    private UUID mainNode;
    private boolean mainNodeRegistered;

    @Contract(pure = true)
    public ModuleManager(Module module) {
        thisModule = module;
        this.modules = new CopyOnWriteArrayList<>();
        this.executorService = Executors.newScheduledThreadPool(1);
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



    public void start() {
        redisClient = new RedisClient(RedisURI.create("redis://"+ CloudAPI.getInstance().getConfiguration().getRedisPassword() +"@"+CloudAPI.getInstance().getConfiguration().getRedisHost()+":"+CloudAPI.getInstance().getConfiguration().getRedisPort()));
        redisClient.setDefaultTimeout(CloudAPI.getInstance().getConfiguration().getRedisDefaultTimeout(),TimeUnit.MILLISECONDS);
        connection = redisClient.connect();
        executorService.scheduleWithFixedDelay(this::update, 0, 1, TimeUnit.MINUTES);
    }

    public void stop() {
        executorService.shutdownNow();

        ModuleDisconnectInfo disconnectInfo = new ModuleDisconnectInfo(thisModule);
        NetworkManager.getInstance().sendPacket(disconnectInfo);

        connection.close();
        redisClient.shutdown();
    }

    public void update() {
        String mainNode = connection.get("main-node-network-id");

        if (isMainNode()) {
            connection.setex("main-node-network-id", 60, thisModule.getNetworkId().toString());
        } else if (mainNode == null) {
            if (!mainNodeRegistered) {
                connection.set("main-node-network-id", thisModule.getNetworkId().toString());

                this.mainNode = thisModule.getNetworkId();

                mainNodeRegistered = true;
            }
            mainNodeRegistered = false;
        } else {
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

    public Optional<Module> getThisModule(UUID networkId) {
        return modules.stream().filter(module -> module.getNetworkId().equals(networkId)).findFirst();
    }


    public Optional<Module> getNodes() {
        return modules.stream().filter(module -> module.getModuleType().equals(ModuleType.NODE)).findFirst();
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
