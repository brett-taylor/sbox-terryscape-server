package content.startingzone.worldobjecthandlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.cache.object.ObjectDefinition;
import com.terryscape.cache.sound.SoundDefinition;
import com.terryscape.cache.world.WorldObjectDefinition;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.chat.dialogue.DialogueManager;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.sound.SoundManager;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.ImmediateTaskStep;
import com.terryscape.game.task.step.impl.NextTickTaskStep;
import com.terryscape.game.task.step.impl.WaitTaskStep;
import com.terryscape.game.task.step.impl.WalkToTaskStep;
import com.terryscape.game.worldobject.WorldObjectInteractionHandler;
import com.terryscape.maths.RandomUtil;
import com.terryscape.net.Client;
import com.terryscape.game.world.Direction;
import com.terryscape.game.world.coordinate.WorldCoordinate;

import java.util.Set;

@Singleton
public class CoinTableWorldObjectInteractionHandler implements WorldObjectInteractionHandler {

    private final DialogueManager dialogueManager;

    private final SoundManager soundManager;

    private final ItemDefinition goldCoinItemDefinition;

    private final SoundDefinition stealCoinsSoundDefinition;

    private final ObjectDefinition coinTableObjectDefinition;

    @Inject
    public CoinTableWorldObjectInteractionHandler(DialogueManager dialogueManager,
                                                  SoundManager soundManager,
                                                  @Named("gold_coin") ItemDefinition goldCoinItemDefinition,
                                                  @Named("steal_coins") SoundDefinition stealCoinsSoundDefinition,
                                                  @Named("coin_table") ObjectDefinition coinTableObjectDefinition) {

        this.dialogueManager = dialogueManager;
        this.soundManager = soundManager;
        this.goldCoinItemDefinition = goldCoinItemDefinition;
        this.stealCoinsSoundDefinition = stealCoinsSoundDefinition;
        this.coinTableObjectDefinition = coinTableObjectDefinition;
    }

    @Override
    public Set<ObjectDefinition> getObjects() {
        return Set.of(coinTableObjectDefinition);
    }

    @Override
    public void invoke(Client client, WorldObjectDefinition worldObjectDefinition) {
        var player = client.getPlayer().orElseThrow();
        var playerInventory = player.getInventory();
        var playerTask = player.getEntity().getComponentOrThrow(TaskComponent.class);
        var playerChat = player.getEntity().getComponentOrThrow(PlayerChatComponent.class);
        var playerMovement = player.getEntity().getComponentOrThrow(MovementComponent.class);
        var playerAnimation = player.getEntity().getComponentOrThrow(AnimationComponent.class);

        var randomAmountToGive = RandomUtil.randomNumber(15, 70);
        var itemDialogue = dialogueManager.builder().item(goldCoinItemDefinition, "You managed to find %s Gold Coins.".formatted(randomAmountToGive));

        // Check if player has a free slot or has item coins already
        var hasSpace = playerInventory.hasItem(goldCoinItemDefinition) || playerInventory.getFreeSlotCount() > 0;
        if (!hasSpace) {
            playerChat.sendGameMessage("You need at least one free inventory spot to loot the table.");
            return;
        }

        playerTask.setCancellablePrimaryTask(
            WalkToTaskStep.worldCoordinate(playerMovement, new WorldCoordinate(15, 15)),

            ImmediateTaskStep.doThis(() -> {
                playerMovement.look(Direction.SOUTH);
                playerAnimation.playAnimation("pickup");
                playerChat.sendGameMessage("You begin to loot the table...");
                soundManager.playSoundEffect(client, stealCoinsSoundDefinition);
            }),

            // Wait
            WaitTaskStep.ticks(3),
            NextTickTaskStep.doThis(() -> {
                playerAnimation.playAnimation("pickup");
                soundManager.playSoundEffect(client, stealCoinsSoundDefinition);
            }),

            WaitTaskStep.ticks(2),
            NextTickTaskStep.doThis(() -> {
                playerInventory.addItem(goldCoinItemDefinition, randomAmountToGive);
                soundManager.playSoundEffect(client, stealCoinsSoundDefinition);
            }),

            dialogueManager.createViewDialogueTaskStep(client, itemDialogue)
        );
    }
}
