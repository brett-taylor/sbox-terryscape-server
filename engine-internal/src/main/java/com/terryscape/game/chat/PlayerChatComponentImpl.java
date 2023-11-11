package com.terryscape.game.chat;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.net.PacketManager;

public class PlayerChatComponentImpl extends BaseEntityComponent implements PlayerChatComponent {

    private final PacketManager packetManager;


    public PlayerChatComponentImpl(Entity entity, PacketManager packetManager) {
        super(entity);
        this.packetManager = packetManager;
    }

    @Override
    public void sendGameMessage(String message) {
        var client = getEntity().getComponentOrThrow(PlayerComponent.class).getClient();

        var playerMessage = new GameMessageOutgoingPacket()
            .setMessage(message);

        packetManager.send(client, playerMessage);
    }

    @Override
    public void handleChat(String message) {
        var username  = getEntity().getComponentOrThrow(PlayerComponent.class).getUsername();

        var gameMessage = new GameMessageOutgoingPacket()
            .setMessage(message)
            .setFromUsername(username);

        packetManager.broadcast(gameMessage);
    }
}
