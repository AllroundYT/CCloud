package de.curse.allround.core.cloud.module;

import lombok.Data;

import java.util.UUID;

@Data
public class Module {
    private final ModuleType moduleType;
    private final UUID networkId;
    private final String name;
}
