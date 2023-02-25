package de.curse.allround.core.cloud.servergroup;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.server.Server;
import de.curse.allround.core.cloud.server.ServerSnapshot;
import de.curse.allround.core.cloud.util.Document;
import de.curse.allround.core.cloud.util.JsonUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NodeGroupManager extends ServerGroupManager {
    /*
     ServerGroups müssen geladen werden, wenn dies der mainnode ist.
     Alle ServerGroups müssen gespeichert werden
    */

    public static final Path GROUPS_DIR = Path.of("Storage", "Groups");

    private final ScheduledExecutorService scheduledExecutorService;

    public NodeGroupManager() {
        super(NodeGroup.class);
        scheduledExecutorService = Executors.newScheduledThreadPool(0);
    }

    public void start() {
        if (CloudAPI.getInstance().getModuleManager().isMainNode()) {
            loadGroups();
        }

        scheduledExecutorService.scheduleWithFixedDelay(this::manageGroups, 1, 1, TimeUnit.MINUTES);
    }

    public void stop() {
        scheduledExecutorService.shutdownNow();
        saveGroups();
    }

    public void loadGroups() {
        if (Files.notExists(GROUPS_DIR)) return;

        try (Stream<Path> stream = Files.list(GROUPS_DIR)) {
            stream.forEach(path -> {
                try (InputStream inputStream = Files.newInputStream(path)) {
                    ServerGroup serverGroup = JsonUtil.GSON_BEAUTIFUL.fromJson(new String(inputStream.readAllBytes()), NodeGroup.class);
                    addGroup(serverGroup);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveGroups() {
        try {
            Files.createDirectories(GROUPS_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }

        getServerGroups().forEach(serverGroup -> {
            try {
                Files.createFile(Path.of(GROUPS_DIR.toString(), serverGroup.getName() + ".json"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (OutputStream outputStream = Files.newOutputStream(Path.of(GROUPS_DIR.toString(), serverGroup.getName() + ".json"))) {
                outputStream.write(JsonUtil.GSON_BEAUTIFUL.toJson(serverGroup).getBytes());
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    //Wird per scheduler aufgerufen
    public void manageGroups() {
        if (!CloudAPI.getInstance().getModuleManager().isMainNode()) return;

        getServerGroups().forEach(serverGroup -> {
            List<Server> servers = (CloudAPI.getInstance().getServerManager().getServers().stream().filter(server -> server.getServerGroup().equals(serverGroup) && !serverGroup.getIgnoredStates().contains(server.getStatus())).toList());
            for (int i = servers.size(); i < serverGroup.getMinServers(); i++) {
                Server server = serverGroup.createServer();
                server.start();
            }
            int serverIndex = 0;
            for (int i = servers.size(); i > serverGroup.getMaxServers(); i--) {
                List<Server> sortedServers = servers.stream().sorted(Comparator.comparingInt(value -> CloudAPI.getInstance().getPlayerManager().getPlayers(value).size())).toList();
                Server server = sortedServers.get(serverIndex);
                server.stop();
                serverIndex++;
            }
        });
    }
}
