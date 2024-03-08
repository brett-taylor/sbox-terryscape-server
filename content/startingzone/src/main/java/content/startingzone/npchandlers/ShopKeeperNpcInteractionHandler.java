package content.startingzone.npchandlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.game.chat.dialogue.DialogueManager;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.game.npc.NpcInteractionHandler;
import com.terryscape.game.shop.Shop;
import com.terryscape.game.shop.ShopManager;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.WalkToTaskStep;
import com.terryscape.net.Client;
import com.terryscape.world.coordinate.WorldCoordinate;
import content.startingzone.shops.EquipmentShop;
import content.startingzone.shops.FoodShop;
import content.startingzone.shops.WeaponShop;

import java.util.Set;

@Singleton
public class ShopKeeperNpcInteractionHandler implements NpcInteractionHandler {

    private final DialogueManager dialogueManager;

    private final ShopManager shopManager;

    private final EquipmentShop equipmentShop;

    private final WeaponShop weaponShop;

    private final FoodShop foodShop;

    @Inject
    public ShopKeeperNpcInteractionHandler(DialogueManager dialogueManager,
                                           ShopManager shopManager,
                                           EquipmentShop equipmentShop,
                                           WeaponShop weaponShop,
                                           FoodShop foodShop) {

        this.dialogueManager = dialogueManager;
        this.shopManager = shopManager;
        this.equipmentShop = equipmentShop;
        this.weaponShop = weaponShop;
        this.foodShop = foodShop;
    }

    @Override
    public Set<String> getNpcIds() {
        return Set.of("armour_shop_keeper", "weapon_shop_keeper", "food_shop_keeper");
    }

    @Override
    public void invoke(Client client, NpcComponent npcComponent) {
        var player = client.getPlayer().orElseThrow();
        var playerTask = player.getEntity().getComponentOrThrow(TaskComponent.class);
        var playerMovement = player.getEntity().getComponentOrThrow(MovementComponent.class);

        var dialogue = dialogueManager.builder()
            .player("Hi, I would like to see your stock please.")
            .npc(npcComponent.getNpcDefinition(), "Certainly.");

        playerTask.setCancellablePrimaryTask(
            WalkToTaskStep.worldCoordinate(playerMovement, getDestinationWorldCoordinate(npcComponent)),

            dialogueManager.createViewDialogueTaskStep(client, dialogue),

            shopManager.createViewShopTaskStep(getShop(npcComponent), client)
        );
    }

    private WorldCoordinate getDestinationWorldCoordinate(NpcComponent npcComponent) {
        return switch (npcComponent.getNpcDefinition().getId()) {
            case "armour_shop_keeper" -> new WorldCoordinate(11, 15);
            case "weapon_shop_keeper" -> new WorldCoordinate(17, 15);
            case "food_shop_keeper" -> new WorldCoordinate(18, 14);
            default -> throw new IllegalStateException("Unexpected value: %s".formatted(npcComponent.getNpcDefinition().getId()));
        };
    }

    private Shop getShop(NpcComponent npcComponent) {
        return switch (npcComponent.getNpcDefinition().getId()) {
            case "armour_shop_keeper" -> equipmentShop;
            case "weapon_shop_keeper" -> weaponShop;
            case "food_shop_keeper" -> foodShop;
            default -> throw new IllegalStateException("Unexpected value: %s".formatted(npcComponent.getNpcDefinition().getId()));
        };
    }
}
