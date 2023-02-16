package de.curse.allround.core.cloud.module;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ModuleInfo extends HashMap<String,Object> {
    public ModuleInfo(UUID networkId, @NotNull ModuleType moduleType, String name) {
        put("network-id",networkId);
        put("module-type",moduleType.name());
        put("name",name);
    }

    public UUID getNetworkId(){
        return (UUID) get("network-id");
    }

    public ModuleType getType(){
        return ModuleType.valueOf((String) get("module-type"));
    }

    public String getName(){
        return (String) get("name");
    }
}
