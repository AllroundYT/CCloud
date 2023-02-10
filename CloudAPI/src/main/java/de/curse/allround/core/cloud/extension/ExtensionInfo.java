package de.curse.allround.core.cloud.extension;

import lombok.*;
import lombok.experimental.Accessors;

import java.nio.file.Path;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Accessors(fluent = true)
public class ExtensionInfo {
    private final Path jarFile;
    private ExtensionInfoFile extensionInfoFile;
    private boolean loaded;
    private boolean enabled;
    private CloudExtension cloudExtension;
}
