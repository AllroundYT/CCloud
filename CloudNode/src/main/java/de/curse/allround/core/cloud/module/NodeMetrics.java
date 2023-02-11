package de.curse.allround.core.cloud.module;

import de.curse.allround.core.cloud.CloudAPI;

import java.util.UUID;

public class NodeMetrics extends ModuleMetrics{
    public NodeMetrics(){
        put("network-id", CloudAPI.getInstance().getModuleManager().getThisModule().getNetworkId());
        put("max_ram", ((int)Runtime.getRuntime().maxMemory()/1024/1024));
        put("ram_in_use",((int)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024/1024));
        put("cpu_load",0.5D);
    }

    public UUID getNetworkId(){
        return (UUID) get("network-id");
    }

    public int getMaxRam(){
        return (int) get("max_ram");
    }

    public int getRamInUse(){
        return (int) get("ram_in_use");
    }

    public double getCPULoad(){
        return (double) get("cpu_load");
    }
}
