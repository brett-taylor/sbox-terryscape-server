package com.terryscape.game.chat;

import com.terryscape.entity.EntityIdentifier;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

public class GameMessageOutgoingPacket implements OutgoingPacket {

    public static GameMessageOutgoingPacket gameMessage(String message) {
        return new GameMessageOutgoingPacket(GameMessageType.GAME_MESSAGE, message, null);
    }

    public static GameMessageOutgoingPacket playerPublicChatMessage(PlayerComponent from, String message) {
        return new GameMessageOutgoingPacket(GameMessageType.PLAYER_PUBLIC_CHAT_MESSAGE, message, from.getEntity().getIdentifier());
    }

    public static GameMessageOutgoingPacket playerOverheadChat(PlayerComponent from, String message) {
        return new GameMessageOutgoingPacket(GameMessageType.OVERHEAD_CHAT, message, from.getEntity().getIdentifier());
    }

    public static GameMessageOutgoingPacket npcOverheadChat(NpcComponent from, String message) {
        return new GameMessageOutgoingPacket(GameMessageType.OVERHEAD_CHAT, message, from.getEntity().getIdentifier());
    }

    private final GameMessageType gameMessageType;

    private final String message;

    private final EntityIdentifier fromEntityIdentifier;

    private GameMessageOutgoingPacket(GameMessageType gameMessageType, String message, EntityIdentifier fromEntityIdentifier) {
        this.gameMessageType = gameMessageType;
        this.message = message;
        this.fromEntityIdentifier = fromEntityIdentifier;
    }

    @Override
    public String getPacketName() {
        return "server_client_game_message";
    }

    @Override
    public void writePacket(OutputStream packet) {
        OutgoingPacket.writeEnum(packet, gameMessageType);
        OutgoingPacket.writeString(packet, message);

        if (fromEntityIdentifier != null) {
            fromEntityIdentifier.writeToPacket(packet);
        }
    }

    private enum GameMessageType {
        GAME_MESSAGE,
        PLAYER_PUBLIC_CHAT_MESSAGE,
        OVERHEAD_CHAT,
    }
}
