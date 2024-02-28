package com.terryscape.game.player;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.entity.EntityIdentifier;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.net.Client;
import com.terryscape.net.IncomingPacket;
import com.terryscape.world.WorldManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;

@Singleton
public class PlayerActionIncomingPacket implements IncomingPacket {

    private static final Logger LOGGER = LogManager.getLogger(PlayerActionIncomingPacket.class);

    private final WorldManager worldManager;

    @Inject
    public PlayerActionIncomingPacket(WorldManager worldManager) {
        this.worldManager = worldManager;
    }

    @Override
    public String getPacketName() {
        return "client_server_player_action";
    }

    @Override
    public void handlePacket(Client client, ByteBuffer packet) {
        var playerIdentifier = EntityIdentifier.readFromPacket(packet);
        var action = IncomingPacket.readString(packet);

        var otherPlayer = worldManager.getEntity(playerIdentifier).getComponentOrThrow(PlayerComponent.class);
        var selfPlayer = client.getPlayer().orElseThrow();

        if (selfPlayer == otherPlayer) {
            LOGGER.warn("Player {} did a player action on themselves.", selfPlayer.getUsername());
            return;
        }

        // TODO check the player can interact with players currently?

        if (action.equals("attack")) {
            var otherPlayerCombat = otherPlayer.getEntity().getComponentOrThrow(CombatComponent.class);
            var selfPlayerCombat = selfPlayer.getEntity().getComponentOrThrow(CombatComponent.class);
            selfPlayerCombat.attack(otherPlayerCombat);
        }
    }
}
