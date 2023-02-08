package de.curse.allround.core.cloud.extension;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

public class ExtensionException extends Exception{
    public ExtensionException(@NotNull ExtensionInfo extensionInfo, @NotNull Reason reason) {
        super(reason.getMsg().replace("%EXTENSION%",extensionInfo.extensionInfoFile().getName()+"-"+extensionInfo.extensionInfoFile().getVersion()));
    }

    @RequiredArgsConstructor
    @Getter
    public static enum Reason{
        NO_MAIN_FOUND("Exception while loading %EXTENSION%: Could not find main class."),
        INSTANCE_COULD_NOT_BE_CREATED("Exception while loading %EXTENSION%: Could not create a new instance of CloudExtension.class."),
        NOT_LOADED("Exception while enabling %EXTENSION%: Could not enable extension because it's not loaded yet.");
        private final String msg;
    }
}
