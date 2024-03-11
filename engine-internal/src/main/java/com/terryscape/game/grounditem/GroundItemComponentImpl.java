package com.terryscape.game.grounditem;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.item.ItemContainerItem;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.world.coordinate.WorldCoordinate;

import java.io.OutputStream;

public class GroundItemComponentImpl extends BaseEntityComponent implements GroundItemComponent {

    private final ItemContainerItem itemContainerItem;

    private final WorldCoordinate worldCoordinate;

    private boolean hasBeenTaken = false;

    public GroundItemComponentImpl(Entity entity, ItemContainerItem itemContainerItem, WorldCoordinate worldCoordinate) {
        super(entity);

        this.itemContainerItem = itemContainerItem;
        this.worldCoordinate = worldCoordinate;
    }

    @Override
    public String getComponentIdentifier() {
        return "component_ground_item";
    }

    public ItemContainerItem getItemContainerItem() {
        return itemContainerItem;
    }

    public WorldCoordinate getWorldCoordinate() {
        return worldCoordinate;
    }

    public void take(PlayerComponent playerComponent) {
        if (hasBeenTaken) {
            playerComponent.getEntity().getComponentOrThrow(PlayerChatComponent.class).sendGameMessage("Its already gone...");
            return;
        }

        var hasSpace = playerComponent.getInventory().hasFreeSlots(1);
        if (!hasSpace) {
            return;
        }

        hasBeenTaken = true;

        playerComponent.getInventory().addItem(itemContainerItem.getItemDefinition(), itemContainerItem.getQuantity());

        getEntity().delete();
    }

    @Override
    public void writeEntityAddedPacket(OutputStream packet) {
        itemContainerItem.writeToPacket(packet);
        worldCoordinate.writeToPacket(packet);
    }

    @Override
    public void writeEntityUpdatedPacket(OutputStream packet) {

    }
}
