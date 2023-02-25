package de.curse.allround.core.cloud.cli.commands;

import de.curse.allround.core.cloud.CloudAPI;
import de.curse.allround.core.cloud.cli.Command;

public class ListServersCommand extends Command {
    public ListServersCommand() {
        super("ListServers","Displays a list of all known servers.",new String[]{"ls"});
    }

    @Override
    public void execute(String[] args) {
        System.out.println("----------------- Servers -----------------");

        if (CloudAPI.getInstance().getServerManager().getServers().size() == 0){
            System.out.println("No known server existing. To create one please use \"createServer <name (String)> <group (String)>\"");
        }else {
            CloudAPI.getInstance().getServerManager().getServers().forEach(server -> {
                System.out.println("Server → Name: "+server.getName()+" → Group: "+server.getServerGroup() +" → Status: "+server.getStatus()+" → Address: "+server.getHost()+":"+server.getPort());
            });
        }

        System.out.println("-------------------------------------------");
    }
}
