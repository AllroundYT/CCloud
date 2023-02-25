package de.curse.allround.core.cloud.proxy;

import de.curse.allround.core.cloud.CloudAPI;

public class NodeProxyManager extends ProxyManager{
    public NodeProxyManager(Class<? extends Proxy> proxyImplClass) {
        super(proxyImplClass);
    }

    @Override
    public Proxy createProxy() {
        return null;
    }

    public void start(){
        for (int i = 0 ; i < Integer.getInteger("node.proxies-to-start",0);i++){
            //Wenn im start befehl angegeben ist, dass proxies gestartet werden sollen passiert dies jetzt
            //Dieses feature kann genutzt werden wenn zusatzt server zui lasten verteilung über ein image gestartet werdem und jeweils einen proxy haben sollen
        }

        if (!CloudAPI.getInstance().getModuleManager().isMainNode()){
            return;
        }

        //Lädt einen Node wenn er soll
    }

    public void stop(){

    }

    /**
     * Wird von main node ausgeführt um zu überwachen, dass immer ein Proxy online ist
     */
    public void update(){
        if (!CloudAPI.getInstance().getModuleManager().isMainNode()) return;
    }
}
