package de.curse.allround.core.cloud.event;

import de.curse.allround.core.cloud.event.listener.EventHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Getter
public class EventBus {

    private final List<Object> listenerObjects;
    private final Map<UUID, EventListener<?>> consumer;

    public EventBus() {
        this.consumer = new HashMap<>();
        this.listenerObjects = new ArrayList<>();
    }

    public void subscribe(@NotNull Class<?> listenerClass){
        try {
            Object listener = listenerClass.getConstructor().newInstance();
            listenerObjects.add(listener);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void unsubscribe(@NotNull Class<?> listenerClass){
        listenerObjects.removeIf(listenerClass::isInstance);
    }

    public <T extends Event> UUID subscribe(Class<T> eventClass, Consumer<T> eventConsumer,Predicate<T> predicate){
        UUID uuid = UUID.randomUUID();
        consumer.put(uuid,new EventListener<T>(eventClass,eventConsumer,predicate));
        return uuid;
    }

    public void unsubscribe(UUID uuid){
        this.consumer.remove(uuid);
    }


    @SuppressWarnings("unchecked")
    public <T extends Event> void call(T event){
        this.listenerObjects.forEach(o -> {
            for (Method method : o.getClass().getMethods()) {
                if (method.isAnnotationPresent(EventHandler.class) && Arrays.equals(method.getParameterTypes(), new Class[]{event.getClass()})){
                    try {
                        if (event.stopped() && !method.getAnnotation(EventHandler.class).ignoreStopped()) return;
                        method.invoke(o,event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        for (EventListener<?> value : this.consumer.values()) {
            try {
                EventListener<T> eventListener = (EventListener<T>) value;
                if (event.getClass().equals(eventListener.getEventClass())) {
                    if (event.stopped()) return;
                    eventListener.handleEvent(event);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @RequiredArgsConstructor
    @AllArgsConstructor
    @Getter
    protected static class EventListener<T extends Event> {
        private final Class<T> eventClass;
        private final Consumer<T> eventConsumer;
        private final Predicate<T> predicate;

        public void handleEvent(T event) {
            if (predicate != null) {
                if (!predicate.test(event)) return;
            }
            eventConsumer.accept(event);
        }
    }
}
