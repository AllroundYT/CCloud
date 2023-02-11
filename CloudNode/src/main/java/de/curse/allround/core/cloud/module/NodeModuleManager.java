package de.curse.allround.core.cloud.module;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.RedisURI;
import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.CloudNode;
import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet_types.module.ModuleDisconnectInfo;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NodeModuleManager extends ModuleManager{
    private final ScheduledExecutorService executorService;
    private RedisClient redisClient;
    private RedisConnection<String, String> connection;
    public NodeModuleManager(Module module) {
        super(module);
        this.executorService = Executors.newScheduledThreadPool(1);
    }
    public void start() {
        redisClient = new RedisClient(RedisURI.create("redis://"+ CloudNode.getInstance().getConfiguration().getRedisPassword() +"@"+CloudNode.getInstance().getConfiguration().getRedisHost()+":"+CloudNode.getInstance().getConfiguration().getRedisPort()));
        redisClient.setDefaultTimeout(CloudNode.getInstance().getConfiguration().getRedisDefaultTimeout(), TimeUnit.MILLISECONDS);
        connection = redisClient.connect();
        executorService.scheduleWithFixedDelay(this::update, 0, 1, TimeUnit.MINUTES);
    }

    public void stop() {
        executorService.shutdownNow();

        ModuleDisconnectInfo disconnectInfo = new ModuleDisconnectInfo(getThisModule());
        NetworkManager.getInstance().sendPacket(disconnectInfo);

        connection.close();
        redisClient.shutdown();
    }

    public void update() {
        String mainNode = connection.get("main-node-network-id");

        if (isMainNode()) {
            connection.setex("main-node-network-id", 60, getThisModule().getNetworkId().toString());
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
}
