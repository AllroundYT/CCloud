package de.curse.allround.core.cloud.network.packet;

import de.curse.allround.core.cloud.network.packet_types.module.ModuleConnectInfo;
import de.curse.allround.core.cloud.network.packet_types.module.ModuleDisconnectInfo;
import de.curse.allround.core.cloud.network.packet_types.player.*;
import de.curse.allround.core.cloud.network.packet_types.proxy.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class PacketConverter {
    private volatile static PacketConverter instance;
    private final HashMap<String, Class<? extends PacketType>> packetTypes;

    @Contract(pure = true)
    public PacketConverter() {
        this.packetTypes = new HashMap<>();
        /* MODULE PACKET TYPES */
        registerType("MODULE_CONNECT_INFO", ModuleConnectInfo.class);
        registerType("MODULE_DISCONNECT_INFO", ModuleDisconnectInfo.class);

        /* PLAYER PACKET TYPES */
        registerType("PLAYER_CONNECT_INFO", PlayerConnectInfo.class);
        registerType("PLAYER_DATA_REQUEST", PlayerDataRequest.class);
        registerType("PLAYER_DATA_RESPONSE", PlayerDataResponse.class);
        registerType("PLAYER_DISCONNECT_INFO", PlayerDisconnectInfo.class);
        registerType("PLAYER_KICK_REQUEST", PlayerKickRequest.class);
        registerType("PLAYER_KICK_RESPONSE",PlayerKickResponse.class);
        registerType("PLAYER_SEND_MSG_REQUEST",PlayerSendMessageRequest.class);
        registerType("PLAYER_SEND_MSG_RESPONSE",PlayerSendMessageResponse.class);
        registerType("PLAYER_SEND_REQUEST",PlayerSendRequest.class);
        registerType("PLAYER_SEND_RESPONSE",PlayerSendResponse.class);
        registerType("PLAYER_SWITCH_SERVER_INFO",PlayerSwitchServerInfo.class);
        registerType("PLAYER_LIST_REQUEST",PlayerListRequest.class);
        registerType("PLAYER_LIST_RESPONSE",PlayerListResponse.class);

        /* PROXY PACKET TYPES */
        registerType("PROXY_CREATE_INFO", ProxyCreateInfo.class);
        registerType("PROXY_DATA_REQUEST", ProxyDataRequest.class);
        registerType("PROXY_DATA_RESPONSE", ProxyDataResponse.class);
        registerType("PROXY_DELETE_INFO", ProxyDeleteInfo.class);
        registerType("PROXY_LIST_REQUEST", ProxyListRequest.class);
        registerType("PROXY_LIST_RESPONSE", ProxyListResponse.class);
        registerType("PROXY_START_REQUEST", ProxyStartRequest.class);
        registerType("PROXY_START_RESPONSE", ProxyStartResponse.class);
        registerType("PROXY_STOP_REQUEST", ProxyStopRequest.class);
        registerType("PROXY_STOP_RESPONSE", ProxyStopResponse.class);
        registerType("PROXY_UPDATE_INFO", ProxyUpdateInfo.class);
    }

    public static PacketConverter getInstance() {
        if (PacketConverter.instance == null) PacketConverter.instance = new PacketConverter();
        return instance;
    }

    @Contract(pure = true)
    public HashMap<String, Class<? extends PacketType>> getPacketTypes() {
        return packetTypes;
    }

    @SuppressWarnings("ALL")
    public PacketConverter registerType(@NotNull String type, @NotNull Class<? extends PacketType> packetType) {
        packetTypes.put(type.toUpperCase(), packetType);
        return this;
    }

    public boolean isConvertable(@NotNull Packet packet) {
        return packetTypes.containsKey(packet.getType());
    }

    @SuppressWarnings("unchecked")
    public <T extends PacketType> T convert(@NotNull Packet packet) {
        Class<T> tClass = (Class<T>) packetTypes.getOrDefault(packet.getType(), PacketType.NULL_TYPE.class);
        try {
            return tClass.getDeclaredConstructor(Packet.class).newInstance(packet);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
