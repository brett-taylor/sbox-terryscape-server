package com.terryscape.game.player;

import com.google.inject.Singleton;
import com.terryscape.game.item.FixedSizeItemContainer;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.world.coordinate.WorldCoordinate;

import java.util.HashMap;

@Singleton
public class TemporaryPlayerSaveSystem {

    private final HashMap<String, SavedPlayer> saves;

    public TemporaryPlayerSaveSystem() {
        this.saves = new HashMap<>();
    }

    public void savePlayer(PlayerComponent playerComponent) {
        var newSaveData = new SavedPlayer(
            playerComponent.getEntity().getComponentOrThrow(MovementComponent.class).getWorldCoordinate(),
            playerComponent.getInventory(),
            (FixedSizeItemContainer) playerComponent.getEquipment()
        );

        saves.put(playerComponent.getSteamId(), newSaveData);
    }

    public void restorePlayerIfHasSave(PlayerComponent playerComponent) {
        if (!saves.containsKey(playerComponent.getSteamId())) {
            return;
        }

        var saveData = saves.get(playerComponent.getSteamId());
        playerComponent.getEntity().getComponentOrThrow(MovementComponent.class).teleport(saveData.position);

        for (var i = 0; i < 28; i++) {
            var item = saveData.playerInventory.getItemAt(i);

            if (item.isPresent()) {
                playerComponent.getInventory().addItemAt(i, item.get().getItemDefinition(), item.get().getQuantity());
            }
        }

        for (var i = 0; i < 7; i++) {
            var item = saveData.playerEquipment.getItemAt(i);

            if (item.isPresent()) {
                ((FixedSizeItemContainer) playerComponent.getEquipment()).addItemAt(i, item.get().getItemDefinition(), item.get().getQuantity());
            }
        }
    }

    private static class SavedPlayer {

        private final WorldCoordinate position;

        private final FixedSizeItemContainer playerInventory;

        private final FixedSizeItemContainer playerEquipment;

        public SavedPlayer(WorldCoordinate position, FixedSizeItemContainer playerInventory, FixedSizeItemContainer playerEquipment) {
            this.position = position;
            this.playerInventory = playerInventory;
            this.playerEquipment = playerEquipment;
        }
    }
}
