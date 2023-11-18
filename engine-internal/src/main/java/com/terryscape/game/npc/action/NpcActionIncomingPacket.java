package com.terryscape.game.npc.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.entity.EntityManager;
import com.terryscape.game.chat.PlayerChatComponent;
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
        var npcIdentifier = IncomingPacket.readEntityIdentifier(packet);
        var action = IncomingPacket.readString(packet);

        var npc = entityManager.getNpc(npcIdentifier);
        // TODO check distance? If too far we should run towards it?
        // TODO check the player can interact with npcs currently?

        var player = client.getPlayer().orElseThrow();

        if (action.equals("attack")) {
            player.getEntity()
                .getComponentOrThrow(PlayerChatComponent.class)
                .sendGameMessage("You killed %s.".formatted(npc.getNpcDefinition().getName()));

            entityManager.deleteEntity(npcIdentifier);
        }

        if (action.equals("examine")) {
            player.getEntity().getComponentOrThrow(PlayerChatComponent.class).sendGameMessage(npc.getNpcDefinition().getDescription());
        }
    }
}
