package de.curse.allround.core.cloud.extension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class NodeExtensionManager extends ExtensionManager{
    @Override
    public void scanForExtensions() {
        try (Stream<Path> paths = Files.list(Path.of("Extensions"))){
            paths.forEach(path -> {
                if (path.toString().endsWith(".jar")){
                    ExtensionInfo extensionInfo = new ExtensionInfo(path);
                    this.extensionInfos.add(extensionInfo);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
