package de.curse.allround.core.cloud.cli.commands;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.cli.Command;

public class ListeGroupsCommand extends Command {
    public ListeGroupsCommand() {
        super("ListGroups","This command lists all known server groups.",new String[]{"lgroup","lg"});
    }

    @Override
    public void execute(String[] args) {
        System.out.println("----------------- NodeList -----------------");
        CloudAPI.getInstance().getServerGroupManager().getServerGroups().forEach(serverGroup -> {
            System.out.println("Group → Name: "+serverGroup.getName()+" → max. Servers: "+serverGroup.getMaxServers()+" → min. Servers: "+serverGroup.getMinServers());
            CloudAPI.getInstance().getServerManager().getServers().stream().filter(server -> server.getServerGroup().equals(serverGroup)).forEach(server -> {
                System.out.println("   ↪ Server → Name: "+server.getName()+" → Node: "+server.getNode()+" → Status: "+server.getStatus()+" → Address: "+server.getHost()+":"+server.getPort());
            });
        });
        if (CloudAPI.getInstance().getServerGroupManager().getServerGroups().size() == 0){
            System.out.println("No server group existing. Please create one with \"createGroup <name (String)> <max ram (int/in Megabyte)> <max players (int)> <min online servers (int)> <max online servers (boolean)> <fallback (boolean)> <lobby (boolean)>\"");
        }
        System.out.println("--------------------------------------------");
    }
}
