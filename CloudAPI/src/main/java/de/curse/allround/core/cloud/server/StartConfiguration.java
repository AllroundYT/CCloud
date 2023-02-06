package de.curse.allround.core.cloud.server;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class StartConfiguration {
    private int maxRam;
    private String motd;
    private int maxPlayers;
    private boolean fallback;
    private boolean lobby;
}
