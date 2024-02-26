package content.startingzone.worldobjecthandlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.cache.world.WorldObjectDefinition;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.chat.dialogue.DialogueManager;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.ImmediateTaskStep;
import com.terryscape.game.task.step.impl.NextTickTaskStep;
import com.terryscape.game.task.step.impl.WaitTaskStep;
import com.terryscape.game.task.step.impl.WalkToTaskStep;
import com.terryscape.game.worldobject.WorldObjectInteractionHandler;
import com.terryscape.maths.RandomUtil;
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

    private final DialogueManager dialogueManager;

    @Inject
    public CoinTableWorldObjectInteractionHandler(CacheLoader cacheLoader, DialogueManager dialogueManager) {
        this.cacheLoader = cacheLoader;
        this.dialogueManager = dialogueManager;
    }

    @Override
    public Set<String> getObjectIds() {
        return Set.of("coin_table");
    }

    @Override
    public void invoke(Client client, WorldObjectDefinition worldObjectDefinition) {
        var goldCoin = cacheLoader.getItem("gold_coin");

        var player = client.getPlayer().orElseThrow();
        var playerInventory = player.getInventory();
        var playerTask = player.getEntity().getComponentOrThrow(TaskComponent.class);
        var playerChat = player.getEntity().getComponentOrThrow(PlayerChatComponent.class);
        var playerMovement = player.getEntity().getComponentOrThrow(MovementComponent.class);
        var playerAnimation = player.getEntity().getComponentOrThrow(AnimationComponent.class);

        var randomAmountToGive = RandomUtil.randomNumber(100, 1000);
        var itemDialogue = dialogueManager.builder().item(goldCoin, "You managed to find %s Gold Coins.".formatted(randomAmountToGive));

        // Check if player has a free slot or has item coins already
        var hasSpace = playerInventory.hasItem(goldCoin) || playerInventory.getFreeSlotCount() > 0;
        if (!hasSpace) {
            playerChat.sendGameMessage("You need at least one free inventory spot to loot the table.");
            return;
        }

        playerTask.setCancellablePrimaryTask(
            WalkToTaskStep.worldCoordinate(playerMovement, new WorldCoordinate(15, 15)),

            NextTickTaskStep.doThis(() -> {
                playerMovement.look(Direction.SOUTH);
                playerAnimation.playAnimation("Sword_Attack_L3");
                playerChat.sendGameMessage("You begin to loot the table...");
            }),

            // Wait
            WaitTaskStep.ticks(1),
            NextTickTaskStep.doThis(() -> playerAnimation.playAnimation("Sword_Attack_L3")),
            WaitTaskStep.ticks(1),
            NextTickTaskStep.doThis(() -> playerAnimation.playAnimation("Sword_Attack_L3")),
            WaitTaskStep.ticks(1),

            ImmediateTaskStep.doThis(() -> playerInventory.addItem(goldCoin, randomAmountToGive)),

            dialogueManager.createViewDialogueTaskStep(client, itemDialogue)
        );
    }
}
