package content.startingzone.npchandlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.game.chat.dialogue.DialogueManager;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.game.npc.NpcInteractionHandler;
import com.terryscape.game.shop.Shop;
import com.terryscape.game.shop.ShopManager;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.WalkToTaskStep;
import com.terryscape.net.Client;
import com.terryscape.game.world.coordinate.WorldCoordinate;
import content.startingzone.shops.EquipmentShop;
import content.startingzone.shops.FoodShop;
import content.startingzone.shops.GeneralStoreShop;
import content.startingzone.shops.WeaponShop;
import jakarta.inject.Named;

import java.util.Set;

@Singleton
public class ShopKeeperNpcInteractionHandler implements NpcInteractionHandler {

    private final DialogueManager dialogueManager;

    private final ShopManager shopManager;

    private final EquipmentShop equipmentShop;

    private final WeaponShop weaponShop;

    private final FoodShop foodShop;

    private final GeneralStoreShop generalStoreShop;

    private final NpcDefinition armourShopKeeperNpcDefinition;

    private final NpcDefinition weaponShopKeeperNpcDefinition;

    private final NpcDefinition foodShopKeeperNpcDefinition;

    private final NpcDefinition generalStoreShopKeeperNpcDefinition;

    @Inject
    public ShopKeeperNpcInteractionHandler(DialogueManager dialogueManager,
                                           ShopManager shopManager,
                                           EquipmentShop equipmentShop,
                                           WeaponShop weaponShop,
                                           FoodShop foodShop,
                                           GeneralStoreShop generalStoreShop,
                                           @Named("armour_shop_keeper") NpcDefinition armourShopKeeperNpcDefinition,
                                           @Named("weapon_shop_keeper") NpcDefinition weaponShopKeeperNpcDefinition,
                                           @Named("food_shop_keeper") NpcDefinition foodShopKeeperNpcDefinition,
                                           @Named("general_store_shop_keeper") NpcDefinition generalStoreShopKeeperNpcDefinition) {

        this.dialogueManager = dialogueManager;
        this.shopManager = shopManager;
        this.equipmentShop = equipmentShop;
        this.weaponShop = weaponShop;
        this.foodShop = foodShop;
        this.generalStoreShop = generalStoreShop;
        this.armourShopKeeperNpcDefinition = armourShopKeeperNpcDefinition;
        this.weaponShopKeeperNpcDefinition = weaponShopKeeperNpcDefinition;
        this.foodShopKeeperNpcDefinition = foodShopKeeperNpcDefinition;
        this.generalStoreShopKeeperNpcDefinition = generalStoreShopKeeperNpcDefinition;
    }

    @Override
    public Set<NpcDefinition> getNpcs() {
        return Set.of(armourShopKeeperNpcDefinition, weaponShopKeeperNpcDefinition, foodShopKeeperNpcDefinition, generalStoreShopKeeperNpcDefinition);
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
            case "general_store_shop_keeper" -> new WorldCoordinate(13, 15);
            default -> throw new IllegalStateException("Unexpected value: %s".formatted(npcComponent.getNpcDefinition().getId()));
        };
    }

    private Shop getShop(NpcComponent npcComponent) {
        return switch (npcComponent.getNpcDefinition().getId()) {
            case "armour_shop_keeper" -> equipmentShop;
            case "weapon_shop_keeper" -> weaponShop;
            case "food_shop_keeper" -> foodShop;
            case "general_store_shop_keeper" -> generalStoreShop;
            default -> throw new IllegalStateException("Unexpected value: %s".formatted(npcComponent.getNpcDefinition().getId()));
        };
    }
}
