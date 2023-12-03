package com.terryscape.game.npc.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.entity.EntityIdentifier;
import com.terryscape.entity.EntityManager;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.health.DamageInformation;
import com.terryscape.game.combat.health.DamageType;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.combat.health.HealthComponentImpl;
import com.terryscape.game.equipment.EquipmentSlot;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.ImmediateStep;
import com.terryscape.game.task.step.impl.WaitStep;
import com.terryscape.net.Client;
import com.terryscape.net.IncomingPacket;

import java.nio.ByteBuffer;

@Singleton
public class NpcActionIncomingPacket implements IncomingPacket {

    private final EntityManager entityManager;

    @Inject
    public NpcActionIncomingPacket(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public String getPacketName() {
        return "client_server_npc_action";
    }

    @Override
    public void handlePacket(Client client, ByteBuffer packet) {
        var npcIdentifier = EntityIdentifier.readFromPacket(packet);
        var action = IncomingPacket.readString(packet);

        var npc = entityManager.getNpc(npcIdentifier);
        var player = client.getPlayer().orElseThrow();

        if (action.equals("examine")) {
            var npcDefinition = npc.getNpcDefinition();
            var description = "%s (id=%s, variant=%s)".formatted(npcDefinition.getDescription(), npcDefinition.getId(), npc.getNpcVariant());
            player.getEntity().getComponentOrThrow(PlayerChatComponent.class).sendGameMessage(description);

            player.getEntity().getComponentOrThrow(HealthComponent.class).takeDamage(new DamageInformation().setAmount(100));
        }

        // TODO check the player can interact with npcs currently?

        if (action.equals("attack")) {
            var npcCombat = npc.getEntity().getComponentOrThrow(CombatComponent.class);
            var playerCombat = player.getEntity().getComponentOrThrow(CombatComponent.class);
            playerCombat.attack(npcCombat);
        }
    }
}
