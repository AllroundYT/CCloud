package de.curse.allround.core.cloud.cli.commands;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.CloudNode;
import de.curse.allround.core.cloud.cli.Command;
import de.curse.allround.core.cloud.module.ModuleType;

import java.util.UUID;

public class ListNodesCommand extends Command {
    public ListNodesCommand() {
        super("ListNodes", "This command displays info about all running CloudNodes", new String[]{});
    }

    @Override
    public void execute(String[] args) {
        System.out.println("----------------- NodeInfo -----------------");
        CloudAPI.getInstance().getModuleManager().getModules().forEach(module -> {
            if (!module.getModuleType().equals(ModuleType.NODE)){
                return;
            }

            UUID networkId = module.getNetworkId();
            String name = module.getName();

            System.out.println("Node → Name: "+name+" → NetworkID: "+networkId+" → MainNode: "+CloudAPI.getInstance().getModuleManager().getMainNode().equals(networkId));
        });
        System.out.println("--------------------------------------------");
    }
}
