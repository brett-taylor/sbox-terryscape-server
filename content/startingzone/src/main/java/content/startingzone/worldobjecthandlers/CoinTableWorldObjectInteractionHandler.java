package content.startingzone.worldobjecthandlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.cache.world.WorldObjectDefinition;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.ImmediateStep;
import com.terryscape.game.task.step.impl.WaitStep;
import com.terryscape.game.task.step.impl.WalkToStep;
import com.terryscape.game.worldobject.WorldObjectInteractionHandler;
import com.terryscape.net.Client;
import com.terryscape.world.Direction;
import com.terryscape.world.coordinate.WorldCoordinate;

import java.util.Set;

@Singleton
public class CoinTableWorldObjectInteractionHandler implements WorldObjectInteractionHandler {

    // TODO: Instead of storing the cache loader here we want to be able to just grab them once on startup.
    // TODO: We need to load the cache before the rest of the server
    // TODO: Then either things can get the cahceLoader and get them when they are constructed with the rest of the server
    // TODO: Or they should be able to DI ItemDefinitions in straight away. That would be better I think.

    private final CacheLoader cacheLoader;

    @Inject
    public CoinTableWorldObjectInteractionHandler(CacheLoader cacheLoader) {
        this.cacheLoader = cacheLoader;
    }

    @Override
    public Set<String> getObjectIds() {
        return Set.of("coin_table");
    }

    @Override
    public void invoke(Client client, WorldObjectDefinition worldObjectDefinition) {
        var basicScimitar = cacheLoader.getItem("basic_scimitar");
        var basicSword = cacheLoader.getItem("basic_sword");

        var player = client.getPlayer().orElseThrow();
        var playerTask = player.getEntity().getComponentOrThrow(TaskComponent.class);
        var playerChat = player.getEntity().getComponentOrThrow(PlayerChatComponent.class);
        var playerMovement = player.getEntity().getComponentOrThrow(MovementComponent.class);
        var playerAnimation = player.getEntity().getComponentOrThrow(AnimationComponent.class);

        playerTask.setCancellablePrimaryTask(
            WalkToStep.worldCoordinate(playerMovement, new WorldCoordinate(15, 15)),

            ImmediateStep.run(() -> {
                playerMovement.look(Direction.SOUTH);
                playerAnimation.playAnimation("Sword_Attack_L3");
                playerChat.sendGameMessage("You begin to loot the table...");
            }),

            // Wait
            WaitStep.ticks(1),
            ImmediateStep.run(() -> playerAnimation.playAnimation("Sword_Attack_L3")),
            WaitStep.ticks(1),
            ImmediateStep.run(() -> playerAnimation.playAnimation("Sword_Attack_L3")),
            WaitStep.ticks(1),

            ImmediateStep.run(() -> {
                playerChat.sendGameMessage("You receive a %s and a %s.".formatted(basicScimitar.getName(), basicSword.getName()));
                player.getInventory().addItem(basicScimitar, 1);
                player.getInventory().addItem(basicSword, 1);
            })
        );
    }
}
