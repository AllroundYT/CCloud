package de.curse.allround.core.cloud.event.events.server;

import de.curse.allround.core.cloud.event.Event;

public class ServerStartEvent implements Event {
    @Override
    public String eventName() {
        return null;
    }

    @Override
    public boolean stopped() {
        return false;
    }

    @Override
    public void setStopped(boolean stopped) {

    }
}
