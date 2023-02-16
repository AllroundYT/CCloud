package de.curse.allround.core.cloud.launcher;

import de.curse.allround.core.cloud.CloudNode;
import org.jetbrains.annotations.Contract;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class NodeLauncher {
    @Contract(pure = true)
    public static void main(String[] args) {

        CloudNode cloudNode = new CloudNode();
        cloudNode.init();
        cloudNode.start();
        Runtime.getRuntime().addShutdownHook(new Thread(cloudNode::stop));
    }
}
