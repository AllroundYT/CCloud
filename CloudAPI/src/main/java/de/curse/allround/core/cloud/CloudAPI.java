package de.curse.allround.core.cloud;

import org.jetbrains.annotations.Contract;

public abstract class CloudAPI {
    private static CloudAPI instance;

    @Contract(pure = true)
    public static CloudAPI getInstance() {
        return instance;
    }

    public static <T extends CloudAPI> void setInstance(T cloudApiInstance) {
        CloudAPI.instance = cloudApiInstance;
    }
}
