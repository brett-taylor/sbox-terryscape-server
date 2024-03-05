package com.terryscape.game.npc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.npc.NpcDefinitionNpcAppearanceType;
import com.terryscape.entity.EntityIdentifier;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.net.Client;
import com.terryscape.net.IncomingPacket;
import com.terryscape.world.WorldManager;

import java.nio.ByteBuffer;

@Singleton
public class NpcActionIncomingPacket implements IncomingPacket {

    private final WorldManager worldManager;

    private final NpcInteractionDispatcher npcInteractionDispatcher;

    @Inject
    public NpcActionIncomingPacket(WorldManager worldManager, NpcInteractionDispatcher npcInteractionDispatcher) {
        this.worldManager = worldManager;
        this.npcInteractionDispatcher = npcInteractionDispatcher;
    }

    @Override
    public String getPacketName() {
        return "client_server_npc_action";
    }

    @Override
    public void handlePacket(Client client, ByteBuffer packet) {
        var npcIdentifier = EntityIdentifier.readFromPacket(packet);
        var action = IncomingPacket.readString(packet);

        var npc = worldManager.getEntity(npcIdentifier).getComponentOrThrow(NpcComponentImpl.class);
        var player = client.getPlayer().orElseThrow();

        // TODO: Refactor this examine and attack into their own handlers

        if (action.equals("examine")) {
            var npcDefinition = npc.getNpcDefinition();

            var extraInformation = "";
            if (npcDefinition.getAppearanceType() == NpcDefinitionNpcAppearanceType.HUMANOID) {
                extraInformation = ", humanoid=true";
            } else if (npcDefinition.getAppearanceType() == NpcDefinitionNpcAppearanceType.SIMPLE) {
                var variant = npc.getEntity().getComponentOrThrow(SimpleNpcAppearanceComponent.class).getVariant();
                extraInformation = ", variant=%s".formatted(variant);
            }

            var chatComponent = player.getEntity().getComponentOrThrow(PlayerChatComponent.class);
            var description = "%s (id=%s%s)".formatted(npcDefinition.getDescription(), npcDefinition.getId(), extraInformation);
            chatComponent.sendGameMessage(description);
        }

        // TODO check the player can interact with npcs currently?

        if (npc.getNpcDefinition().isAttackable() && action.equals("attack")) {
            var npcCombat = npc.getEntity().getComponentOrThrow(CombatComponent.class);
            var playerCombat = player.getEntity().getComponentOrThrow(CombatComponent.class);
            playerCombat.attack(npcCombat);
        }

        if (npc.getNpcDefinition().isInteractable() && action.equals("interact")) {
            npcInteractionDispatcher.dispatchNpcInteraction(client, npc);
        }
    }
}
