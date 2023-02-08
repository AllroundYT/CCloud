package de.curse.allround.core.cloud.extension;

import de.curse.allround.core.util.JsonUtil;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

@Getter
public abstract class ExtensionManager {

    private final List<ExtensionInfo> extensionInfos;

    @Contract(pure = true)
    public ExtensionManager() {
        this.extensionInfos = new ArrayList<>();
    }

    public List<ExtensionInfo> getEnabledExtensions(){
        return extensionInfos.stream().filter(ExtensionInfo::enabled).collect(Collectors.toList());
    }

    public List<ExtensionInfo> getLoadedExtensions(){
        return extensionInfos.stream().filter(ExtensionInfo::loaded).collect(Collectors.toList());
    }

    public abstract void scanForExtensions();

    public void loadAll(){
        extensionInfos.forEach(extensionInfo -> {
            try {
                loadExtension(extensionInfo);
            } catch (ExtensionException ignored) {}
        });
    }

    public void enableAll(){
        extensionInfos.forEach(extensionInfo -> {
            try {
                enableExtension(extensionInfo);
            } catch (ExtensionException ignored) {}
        });
    }

    public void disableAll(){
        extensionInfos.forEach(extensionInfo -> {
            try {
                disableExtension(extensionInfo);
            } catch (ExtensionException ignored) {}
        });
    }

    public void loadExtension(ExtensionInfo extensionInfo) throws ExtensionException {
        if (extensionInfos.contains(extensionInfo)) return;

        try (JarFile jarFile = new JarFile(extensionInfo.jarFile().toFile());){

            JarEntry jarEntry = jarFile.getJarEntry("cloud.json");

            try (InputStream inputStream = jarFile.getInputStream(jarEntry)){
                extensionInfo.extensionInfoFile(JsonUtil.GSON.fromJson(new String(inputStream.readAllBytes()),ExtensionInfoFile.class));
            }

            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{extensionInfo.jarFile().toFile().toURI().toURL()});
            Class<?> main = urlClassLoader.loadClass(extensionInfo.extensionInfoFile().getMainClass());

            if (!CloudExtension.class.isAssignableFrom(main)){
                throw new ExtensionException(extensionInfo, ExtensionException.Reason.NO_MAIN_FOUND);
            }

            extensionInfo.cloudExtension((CloudExtension) main.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
            extensionInfo.cloudExtension().load();
            extensionInfo.loaded(true);
        } catch (IOException | NoSuchMethodException | ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ExtensionException(extensionInfo, ExtensionException.Reason.INSTANCE_COULD_NOT_BE_CREATED);
        }
    }

    public void enableExtension(@NotNull ExtensionInfo extensionInfo) throws ExtensionException{
        if (!extensionInfo.loaded()) throw new ExtensionException(extensionInfo, ExtensionException.Reason.NOT_LOADED);
        extensionInfo.cloudExtension().enable();
        extensionInfo.enabled(true);
    }

    public void disableExtension(@NotNull ExtensionInfo extensionInfo) throws ExtensionException{
        if (!extensionInfo.loaded()) throw new ExtensionException(extensionInfo, ExtensionException.Reason.NOT_LOADED);
        extensionInfo.cloudExtension().disable();
        extensionInfo.enabled(false);
    }
}
