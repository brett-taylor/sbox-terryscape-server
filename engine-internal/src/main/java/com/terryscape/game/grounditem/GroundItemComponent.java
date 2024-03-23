package com.terryscape.game.grounditem;

import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.component.EntityComponent;
import com.terryscape.game.item.ItemContainerItem;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.game.world.coordinate.WorldCoordinate;

public class GroundItemComponent extends BaseEntityComponent implements EntityComponent {

    private final ItemContainerItem itemContainerItem;

    private final WorldCoordinate worldCoordinate;

    private PlayerComponent takenBy;

    public GroundItemComponent(ItemContainerItem itemContainerItem,
                               WorldCoordinate worldCoordinate) {

        this.itemContainerItem = itemContainerItem;
        this.worldCoordinate = worldCoordinate;
    }

    public ItemContainerItem getItemContainerItem() {
        return itemContainerItem;
    }

    public WorldCoordinate getWorldCoordinate() {
        return worldCoordinate;
    }

    public PlayerComponent getTakenBy() {
        return takenBy;
    }

    public void take(PlayerComponent playerComponent) {
        if (getTakenBy() != null) {
            return;
        }

        var hasSpace = playerComponent.getInventory().hasFreeSlots(1);
        if (!hasSpace) {
            return;
        }

        takenBy = playerComponent;
    }
}
