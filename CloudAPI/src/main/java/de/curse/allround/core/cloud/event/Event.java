package de.curse.allround.core.cloud.event;

//TODO: Events implementieren
public interface Event {
    String eventName();
    boolean stopped();
    void setStopped(boolean stopped);
}
