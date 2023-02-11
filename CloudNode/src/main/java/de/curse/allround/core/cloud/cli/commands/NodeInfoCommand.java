package de.curse.allround.core.cloud.cli.commands;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.CloudNode;
import de.curse.allround.core.cloud.cli.Command;
import de.curse.allround.core.cloud.module.ModuleType;

import java.util.UUID;

public class NodeInfoCommand extends Command {
    public NodeInfoCommand() {
        super("NodeInfo", "This command displays info about alle running CloudNodes", new String[]{});
    }

    @Override
    public void execute(String[] args) {
        CloudNode.LOGGER.info("----------------- NodeInfo -----------------");
        CloudAPI.getInstance().getModuleManager().getModules().forEach(module -> {
            if (!module.getModuleType().equals(ModuleType.NODE)){
                return;
            }

            UUID networkId = module.getNetworkId();
            String name = module.getName();

            CloudNode.LOGGER.info("CloudNode -> Name: "+name+" -> NetworkID: "+networkId+" -> MainNode: "+CloudAPI.getInstance().getModuleManager().getMainNode().equals(networkId));
        });
        CloudNode.LOGGER.info("--------------------------------------------");
    }
}
