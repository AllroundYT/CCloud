package de.curse.allround.core.cloud.servergroup;

import de.curse.allround.core.cloud.CloudAPI;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NodeGroupManager extends ServerGroupManager {
    /*
     ServerGroups müssen geladen werden, wenn dies der mainnode ist.
     Alle ServerGroups müssen gespeichert werden
    */

    private final ScheduledExecutorService scheduledExecutorService;

    public NodeGroupManager() {
        scheduledExecutorService = Executors.newScheduledThreadPool(0);
    }

    public void start() {
        if (CloudAPI.getInstance().getModuleManager().isMainNode()){
            loadGroups();
        }

        scheduledExecutorService.scheduleWithFixedDelay(this::manageGroups,1,1, TimeUnit.MINUTES);
    }

    public void stop() {
        scheduledExecutorService.shutdownNow();
        saveGroups();
    }

    public void loadGroups() {

    }

    public void saveGroups() {

    }

    //Wird per scheduler aufgerufen
    public void manageGroups() {
        if (!CloudAPI.getInstance().getModuleManager().isMainNode()) return;

        getServerGroups().forEach(serverGroup -> {

        });
    }
}
