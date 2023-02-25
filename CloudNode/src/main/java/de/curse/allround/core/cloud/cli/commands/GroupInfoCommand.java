package de.curse.allround.core.cloud.cli.commands;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.cli.Command;
import de.curse.allround.core.cloud.servergroup.ServerGroup;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class GroupInfoCommand extends Command {
    public GroupInfoCommand() {
        super("GroupInfo","Displays detailed information about a specific server group.", new String[]{"gi"});
    }

    @Override
    public void execute(String @NotNull [] args) {

        if (args.length == 0 || CloudAPI.getInstance().getServerGroupManager().getGroup(args[0]).isEmpty()){
            System.out.println("Please enter a valid server group. \"GroupInfo <server group>\"");
            return;
        }

        ServerGroup serverGroup = CloudAPI.getInstance().getServerGroupManager().getGroup(args[0]).get();

        System.out.println("--------- Server Group Information ---------");
        System.out.println("Name → "+serverGroup.getName());
        System.out.println("Min. Server → "+serverGroup.getMinServers());
        System.out.println("Max. Server → "+serverGroup.getMaxServers());
        System.out.println("Ignored server states → "+ Arrays.toString(serverGroup.getIgnoredStates().toArray()));
        System.out.println("Start configuration:");
        System.out.println("    ↪ MOTD → "+serverGroup.getDefaultStartConfiguration().getMotd());
        System.out.println("    ↪ max. Ram → "+serverGroup.getDefaultStartConfiguration().getMaxRam());
        System.out.println("    ↪ max. Players → "+serverGroup.getDefaultStartConfiguration().getMaxPlayers());
        System.out.println("Servers:");
        CloudAPI.getInstance().getServerManager().getServers().stream().filter(server -> server.getServerGroup().equals(serverGroup)).forEach(server -> {
            System.out.println("    ↪ Name → "+server.getName() + ", Status → "+server.getStatus() + ", Node → "+server.getNode() +
                    (serverGroup.getIgnoredStates().stream().anyMatch(s -> s.equalsIgnoreCase(server.getName())) ? ", (Ignored)" : "")
            );
        });
        System.out.println("--------------------------------------------");
    }
}
