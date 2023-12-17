package com.terryscape.game.chat;

import com.terryscape.entity.Entity;
import com.terryscape.game.BaseEntityComponentImpl;
import com.terryscape.game.chat.command.CommandManager;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.net.PacketManager;

public class PlayerChatComponentImpl extends BaseEntityComponentImpl implements PlayerChatComponent {

    private final PacketManager packetManager;

    private final CommandManager commandManager;

    public PlayerChatComponentImpl(Entity entity, PacketManager packetManager, CommandManager commandManager) {
        super(entity);
        this.packetManager = packetManager;
        this.commandManager = commandManager;
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
        var playerComponent = getEntity().getComponentOrThrow(PlayerComponent.class);

        if (commandManager.checkForCommandPhaseAndExecuteIfFound(playerComponent, message)) {
            return;
        }

        var gameMessage = new GameMessageOutgoingPacket()
            .setMessage(message)
            .setFromUsername(playerComponent.getUsername());

        packetManager.broadcast(gameMessage);
    }
}
