package de.curse.allround.core.cloud.module;

import de.curse.allround.core.cloud.CloudAPI;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class NodeInfo extends ModuleInfo {
    public NodeInfo(UUID networkId, @NotNull ModuleType moduleType, String name){
        super(networkId,moduleType,name);
        put("max_ram", ((int)Runtime.getRuntime().maxMemory()/1024/1024));
        put("ram_in_use",((int)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024/1024));
    }

    public int getMaxRam(){
        return (int) get("max_ram");
    }

    public int getRamInUse(){
        return (int) get("ram_in_use");
    }

}
