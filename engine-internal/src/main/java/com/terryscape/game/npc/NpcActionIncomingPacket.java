package com.terryscape.game.npc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.npc.NpcDefinitionNpcAppearanceType;
import com.terryscape.entity.EntityIdentifier;
import com.terryscape.entity.EntityManager;
import com.terryscape.game.chat.PlayerChatSystem;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.net.Client;
import com.terryscape.net.IncomingPacket;

import java.nio.ByteBuffer;

@Singleton
public class NpcActionIncomingPacket implements IncomingPacket {

    private final EntityManager entityManager;

    private final NpcInteractionDispatcher npcInteractionDispatcher;

    private final PlayerChatSystem playerChatSystem;

    @Inject
    public NpcActionIncomingPacket(EntityManager entityManager, NpcInteractionDispatcher npcInteractionDispatcher, PlayerChatSystem playerChatSystem) {
        this.entityManager = entityManager;
        this.npcInteractionDispatcher = npcInteractionDispatcher;
        this.playerChatSystem = playerChatSystem;
    }

    @Override
    public String getPacketName() {
        return "client_server_npc_action";
    }

    @Override
    public void handlePacket(Client client, ByteBuffer packet) {
        var npcIdentifier = EntityIdentifier.readFromPacket(packet);
        var action = IncomingPacket.readString(packet);

        var npc = entityManager.getEntity(npcIdentifier).getComponentOrThrow(NpcComponentImpl.class);
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

            var description = "%s (id=%s%s)".formatted(npcDefinition.getDescription(), npcDefinition.getId(), extraInformation);
            playerChatSystem.sendGameMessage(player, description);
        }

        if (!player.canDoActions()) {
            return;
        }

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
