package de.curse.allround.core.cloud.proxy;

public class ProxySnapshot extends Proxy{
    public ProxySnapshot(String name, String motd, int maxRam, int maxPlayers, boolean maintenance, String status, String host, int port) {
        super(name, motd, maxRam, maxPlayers, maintenance, status, host, port);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
