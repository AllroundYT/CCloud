package de.curse.allround.core.cloud.cli.commands;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.cli.Command;
import de.curse.allround.core.cloud.network.packet.NetworkManager;
import de.curse.allround.core.cloud.network.packet_types.group.GroupTemplateUpdateInfo;
import de.curse.allround.core.cloud.servergroup.ServerGroup;
import org.jetbrains.annotations.NotNull;

public class BroadcastTemplateUpdateCommand extends Command {
    public BroadcastTemplateUpdateCommand() {
        super("BroadcastTemplateUpdate", "Sends a group template update info packet to all other cloud nodes.", new String[]{"btu","TemplateUpdate"});
    }

    @Override
    public void execute(String @NotNull [] args) {
        if (args.length == 0 || CloudAPI.getInstance().getServerGroupManager().getGroup(args[0]).isEmpty()){
            System.out.println("Please enter a valid server group. \"GroupInfo <server group>\"");
            return;
        }

        ServerGroup serverGroup = CloudAPI.getInstance().getServerGroupManager().getGroup(args[0]).get();

        NetworkManager.getInstance().sendPacket(new GroupTemplateUpdateInfo(serverGroup.getName()));

        System.out.println("Server group template update broadcasted.");
    }
}
