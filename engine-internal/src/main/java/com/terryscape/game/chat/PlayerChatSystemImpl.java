package com.terryscape.game.chat;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.game.chat.command.CommandManager;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.net.PacketManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.terryscape.game.chat.GameMessageOutgoingPacket.*;

@Singleton
public class PlayerChatSystemImpl implements PlayerChatSystem {

    private static final Logger LOGGER = LogManager.getLogger(PlayerChatSystemImpl.class);

    private static final int PLAYER_MESSAGE_LENGTH_LIMIT = 120;

    private final PacketManager packetManager;

    private final CommandManager commandManager;

    @Inject
    public PlayerChatSystemImpl(PacketManager packetManager, CommandManager commandManager) {
        this.packetManager = packetManager;
        this.commandManager = commandManager;
    }

    @Override
    public void sendGameMessage(PlayerComponent playerComponent, String message) {
        packetManager.send(playerComponent.getClient(), gameMessage(message));
    }

    @Override
    public void handleChat(PlayerComponent playerComponent, String message) {
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

    @Override
    public void sendOverheadText(PlayerComponent playerComponent, String message) {
        packetManager.broadcast(playerOverheadChat(playerComponent, message));
    }
}
