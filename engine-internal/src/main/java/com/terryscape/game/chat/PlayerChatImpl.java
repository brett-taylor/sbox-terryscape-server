package com.terryscape.game.chat;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.terryscape.entity.player.Player;
import com.terryscape.net.PacketManager;
import com.terryscape.game.chat.packet.GameMessageOutgoingPacket;

public class PlayerChatImpl implements PlayerChat {

    public interface PlayerChatImplFactory {
        PlayerChatImpl create(Player player);
    }

    private final PacketManager packetManager;

    private final Player player;

    @Inject
    public PlayerChatImpl(PacketManager packetManager, @Assisted Player player) {
        this.packetManager = packetManager;
        this.player = player;
    }

    @Override
    public void sendGameMessage(String message) {
        var playerMessage = new GameMessageOutgoingPacket()
            .setMessage(message);

        packetManager.send(player.getClient(), playerMessage);
    }

    @Override
    public void sendPlayerMessage(String message) {
        var gameMessage = new GameMessageOutgoingPacket()
            .setMessage(message)
            .setFromUsername(player.getUsername());

        packetManager.broadcast(gameMessage);
    }
}
