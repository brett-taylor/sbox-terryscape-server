package com.terryscape.game.grounditem;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.entity.EntityIdentifier;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.ImmediateTaskStep;
import com.terryscape.game.task.step.impl.WalkToTaskStep;
import com.terryscape.net.Client;
import com.terryscape.net.IncomingPacket;
import com.terryscape.entity.EntityManager;

import java.nio.ByteBuffer;

@Singleton
public class GroundItemActionIncomingPacket implements IncomingPacket {

    private final EntityManager entityManager;

    @Inject
    public GroundItemActionIncomingPacket(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public String getPacketName() {
        return "client_server_ground_item_action";
    }

    @Override
    public void handlePacket(Client client, ByteBuffer packet) {
        var groundItemIdentifier = EntityIdentifier.readFromPacket(packet);
        var action = IncomingPacket.readString(packet);

        var groundItem = entityManager.getEntity(groundItemIdentifier).getComponentOrThrow(GroundItemComponent.class);
        var player = client.getPlayer().orElseThrow();

        if (action.equals("examine")) {
            handleExamine(groundItem, player);
            return;
        }

        if (!player.canDoActions()) {
            return;
        }

        if (action.equals("take")) {
            handleTake(groundItem, player);
        }
    }

    private void handleExamine(GroundItemComponent groundItemComponent, PlayerComponent playerComponent) {
        var itemName = groundItemComponent.getItemContainerItem().getItemDefinition().getName();
        var quantity = groundItemComponent.getItemContainerItem().getQuantity();

        var chatComponent = playerComponent.getEntity().getComponentOrThrow(PlayerChatComponent.class);
        chatComponent.sendGameMessage("%s x%s".formatted(itemName, quantity));
    }

    private void handleTake(GroundItemComponent groundItemComponent, PlayerComponent playerComponent) {
        var taskComponent = playerComponent.getEntity().getComponentOrThrow(TaskComponent.class);
        var movementComponent = playerComponent.getEntity().getComponentOrThrow(MovementComponent.class);

        taskComponent.setCancellablePrimaryTask(
            WalkToTaskStep.worldCoordinate(movementComponent, groundItemComponent.getWorldCoordinate()),
            ImmediateTaskStep.doThis(() -> groundItemComponent.take(playerComponent))
        );
    }
}
