package content.startingzone.npchandlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.game.chat.dialogue.PlayerDialogueComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.game.npc.NpcInteractionHandler;
import com.terryscape.game.shop.ShopManager;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.WalkToTaskStep;
import com.terryscape.net.Client;
import content.startingzone.shops.EquipmentShop;
import content.startingzone.shops.WeaponShop;

import java.util.Set;

@Singleton
public class ShopKeeperNpcInteractionHandler implements NpcInteractionHandler {

    private final ShopManager shopManager;

    private final EquipmentShop equipmentShop;

    private final WeaponShop weaponShop;

    @Inject
    public ShopKeeperNpcInteractionHandler(ShopManager shopManager, EquipmentShop equipmentShop, WeaponShop weaponShop) {
        this.shopManager = shopManager;
        this.equipmentShop = equipmentShop;
        this.weaponShop = weaponShop;
    }

    @Override
    public Set<String> getNpcIds() {
        return Set.of("armour_shop_keeper", "weapon_shop_keeper");
    }

    @Override
    public void invoke(Client client, NpcComponent npcComponent) {
        var player = client.getPlayer().orElseThrow();
        var playerTask = player.getEntity().getComponentOrThrow(TaskComponent.class);
        var playerMovement = player.getEntity().getComponentOrThrow(MovementComponent.class);
        var playerDialogue = player.getEntity().getComponentOrThrow(PlayerDialogueComponent.class);

        var npcWorldCoordinate = npcComponent.getEntity().getComponentOrThrow(MovementComponent.class).getWorldCoordinate();
        var destinationTile = npcWorldCoordinate.getClosestCardinalNeighbourFrom(playerMovement.getWorldCoordinate());

        var dialogue = playerDialogue.builder()
            .player("Hi, I would like to see your stock please.")
            .npc(npcComponent.getNpcDefinition(), "Certainly.");

        var viewShopTaskStep = npcComponent.getNpcDefinition().getId().equals("armour_shop_keeper")
            ? shopManager.createViewShopTaskStep(equipmentShop, client)
            : shopManager.createViewShopTaskStep(weaponShop, client);

        playerTask.setCancellablePrimaryTask(
            WalkToTaskStep.worldCoordinate(playerMovement, destinationTile),

            playerDialogue.createViewDialogueTaskStep(dialogue),

            viewShopTaskStep
        );
    }
}
