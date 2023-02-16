package de.curse.allround.core.cloud.cli.commands;

import de.curse.allround.core.cloud.CloudNode;
import de.curse.allround.core.cloud.cli.Command;

import java.util.Arrays;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("Help", "Description to display in help screen.", new String[]{"?"});
    }

    @Override
    public void execute(String[] args) {
        System.out.println("------------------- Help -------------------");
        CloudNode.getInstance().getCommandManager().getCommands().forEach(command -> System.out.println("Command → Name: "+command.getName()+" → Description: "+command.getDescription()+" → Aliases: "+ Arrays.toString(command.getAlias())));
        System.out.println("--------------------------------------------");
    }
}
