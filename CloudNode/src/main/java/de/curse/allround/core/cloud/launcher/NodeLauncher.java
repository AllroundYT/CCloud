package de.curse.allround.core.cloud.launcher;

import org.jetbrains.annotations.Contract;
import org.slf4j.LoggerFactory;

public class NodeLauncher {
    @Contract(pure = true)
    public static void main(String[] args) {
        LoggerFactory.getLogger(NodeLauncher.class).info("TEST");
        /*
        CloudNode cloudNode = new CloudNode();
        cloudNode.init();
        cloudNode.start();
        Runtime.getRuntime().addShutdownHook(new Thread(cloudNode::stop));

         */
    }
}
