package de.curse.allround.core.cloud.config;

import de.curse.allround.core.cloud.util.Document;

import java.io.IOException;
import java.nio.file.Path;

public class NodeConfiguration extends Document {

    public void load(Path path){
        try {
            read(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        addIfNotExists("server-group-template-update-interval",60);

        addIfNotExists("redis-user","cloud");
        addIfNotExists("redis-password","password");
        addIfNotExists("redis-host","127.0.0.1");
        addIfNotExists("redis-port",5555);
        addIfNotExists("redis-default-timeout",10000);

        addIfNotExists("rabbitmq-host","127.0.0.1");
        addIfNotExists("rabbitmq-port",6845);
        addIfNotExists("rabbitmq-user","cloud");
        addIfNotExists("rabbitmq-password","password");
        addIfNotExists("rabbitmq-connection-timeout",10000);

        write(path);
    }

    /**
     * Dieser Wert gibt an wie viele Minuten es dauert bis Gruppen Templates welche verändert wurden automatisch heruntergeladen werden.
     * @return Zeit Interval in Minuten
     */
    public int getServerGroupTemplateUpdateInterval(){
        return getInteger("server-group-template-update-interval");
    }

    public String getRabbitMQHost(){
        return getString("rabbitmq-host");
    }

    public int getRabbitMQPort(){
        return getInteger("rabbitmq-port");
    }

    public String getRedisUser(){
        return getString("redis-user");
    }

    public String getRedisPassword() {
        return getString("redis-password");
    }

    public String getRedisHost(){
        return getString("redis-host");
    }

    public int getRedisPort(){
        return getInteger("redis-port");
    }

    public int getRabbitMQConnectionTimeout() {
        return getInteger("rabbitmq-connection-timeout");
    }

    public String getRabbitMQUser() {
        return getString("rabbitmq-user");
    }

    public String getRabbitMQPassword(){
        return getString("rabbitmq-password");
    }

    public long getRedisDefaultTimeout() {
        return getLong("redis-default-timeout");
    }

}
