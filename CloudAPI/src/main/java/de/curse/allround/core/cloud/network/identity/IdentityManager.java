package de.curse.allround.core.cloud.network.identity;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class IdentityManager {
    private final Map<UUID, String> identities;

    public IdentityManager() {
        this.identities = new HashMap<>();

        UUID thisIdentity = UUID.randomUUID();
        addIdentity(thisIdentity);
        setNotice(thisIdentity, "this");
    }

    public UUID getThisIdentity() {
        return getIdentityByNotice("this").get(0);
    }

    public boolean addIdentity(UUID uuid) {
        if (identities.containsKey(uuid)) return false;
        identities.put(uuid, "");
        return true;
    }

    public void setNotice(UUID identity, String notice) {
        identities.put(identity, notice);
    }

    public String getNotice(UUID identity) {
        return identities.getOrDefault(identity, "");
    }

    public List<UUID> getIdentityByNotice(String notice) {
        return identities.keySet().stream().filter(uuid -> identities.get(uuid).equals(notice)).collect(Collectors.toList());
    }
}
