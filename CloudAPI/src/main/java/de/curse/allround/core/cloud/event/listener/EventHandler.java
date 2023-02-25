package de.curse.allround.core.cloud.event.listener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
    boolean ignoreStopped() default false;
}
