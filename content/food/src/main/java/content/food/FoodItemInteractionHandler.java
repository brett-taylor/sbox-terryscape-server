package content.food;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.item.FixedSizeItemContainer;
import com.terryscape.game.item.ItemInteractionHandler;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.net.Client;
import com.terryscape.util.EnumValueRetriever;

import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class FoodItemInteractionHandler implements ItemInteractionHandler {

    private final EnumValueRetriever enumValueRetriever;

    @Inject
    public FoodItemInteractionHandler(EnumValueRetriever enumValueRetriever) {
        this.enumValueRetriever = enumValueRetriever;
    }

    @Override
    public Set<String> getItemIds() {
        return enumValueRetriever
            .getEnumValues(Food.class).stream()
            .map(Food::getItemId)
            .collect(Collectors.toSet());
    }

    @Override
    public void invoke(Client client, ItemDefinition itemDefinition, FixedSizeItemContainer playerInventory, int inventorySlot) {
        var playerComponent = client.getPlayer().orElseThrow();
        var playerEntity = playerComponent.getEntity();

        var eatDelayComponent = playerComponent.getEntity().getComponent(EatDelayComponent.class);
        if (eatDelayComponent.isPresent() && !eatDelayComponent.get().hasNotifiedPlayer()) {
            if (!eatDelayComponent.get().hasNotifiedPlayer()) {
                eatDelayComponent.get().setNotifiedPlayer(true);
                playerEntity.getComponentOrThrow(PlayerChatComponent.class).sendGameMessage("You can't eat right now...");
            }

            return;
        }

        var foodType = getFoodType(itemDefinition);

        playerEntity.addComponent(new EatDelayComponent(playerEntity, foodType.getEatDelayTicks()));
        playerEntity.getComponentOrThrow(CombatComponent.class).ensureCooldownOfAtLeast(foodType.getCombatDelayTicks());

        playerInventory.removeItemAt(inventorySlot);

        playerEntity.getComponentOrThrow(AnimationComponent.class).playAnimation("drink1");
        playerEntity.getComponentOrThrow(HealthComponent.class).heal(foodType.getHealAmount());
    }

    private Food getFoodType(ItemDefinition itemDefinition) {
        return enumValueRetriever.getEnumValues(Food.class).stream()
            .filter(food -> food.getItemId().equals(itemDefinition.getId()))
            .findFirst()
            .orElseThrow();
    }
}
