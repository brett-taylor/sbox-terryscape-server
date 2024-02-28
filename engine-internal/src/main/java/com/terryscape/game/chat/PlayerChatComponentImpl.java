package com.terryscape.game.chat;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.chat.command.CommandManager;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.net.PacketManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.terryscape.game.chat.GameMessageOutgoingPacket.*;

public class PlayerChatComponentImpl extends BaseEntityComponent implements PlayerChatComponent {

    private static final Logger LOGGER = LogManager.getLogger(PlayerChatComponent.class);

    private final static int PLAYER_MESSAGE_LENGTH_LIMIT = 50;

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

        packetManager.send(client, gameMessage(message));
    }

    @Override
    public void handleChat(String message) {
        var playerComponent = getEntity().getComponentOrThrow(PlayerComponent.class);

        if (commandManager.checkForCommandPhaseAndExecuteIfFound(playerComponent, message)) {
            return;
        }

        message = message.trim();
        message = StringUtils.truncate(message, PLAYER_MESSAGE_LENGTH_LIMIT);

        if (StringUtils.isBlank(message)) {
            return;
        }

        LOGGER.info("Player {} said {}", playerComponent.getUsername(), message);
        packetManager.broadcast(playerPublicChatMessage(playerComponent, message));
    }
}
