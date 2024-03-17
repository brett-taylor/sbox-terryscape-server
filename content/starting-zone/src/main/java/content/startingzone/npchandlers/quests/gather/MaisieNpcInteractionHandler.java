package content.startingzone.npchandlers.quests.gather;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.game.chat.dialogue.DialogueManager;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.game.npc.NpcInteractionHandler;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.ImmediateTaskStep;
import com.terryscape.game.task.step.impl.LookAtTaskStep;
import com.terryscape.game.task.step.impl.WalkToTaskStep;
import com.terryscape.net.Client;

import java.util.Set;

@Singleton
public class MaisieNpcInteractionHandler implements NpcInteractionHandler {

    private final DialogueManager dialogueManager;

    private final ItemDefinition goldCoinItemDefinition;

    private final ItemDefinition mapleLogsItemDefinition;

    private final NpcDefinition maisieNpcDefinition;

    @Inject
    public MaisieNpcInteractionHandler(DialogueManager dialogueManager,
                                       @Named("gold_coin") ItemDefinition goldCoinItemDefinition,
                                       @Named("maple_logs") ItemDefinition mapleLogsItemDefinition,
                                       @Named("maisie") NpcDefinition maisieNpcDefinition) {

        this.dialogueManager = dialogueManager;
        this.goldCoinItemDefinition = goldCoinItemDefinition;
        this.mapleLogsItemDefinition = mapleLogsItemDefinition;
        this.maisieNpcDefinition = maisieNpcDefinition;
    }

    @Override
    public Set<NpcDefinition> getNpcs() {
        return Set.of(maisieNpcDefinition);
    }

    @Override
    public void invoke(Client client, NpcComponent npcComponent) {
        var player = client.getPlayer().orElseThrow();
        var playerTask = player.getEntity().getComponentOrThrow(TaskComponent.class);
        var playerMovement = player.getEntity().getComponentOrThrow(MovementComponent.class);

        var npcWorldCoordinate = npcComponent.getEntity().getComponentOrThrow(MovementComponent.class).getWorldCoordinate();
        var destinationTile = npcWorldCoordinate.getClosestCardinalNeighbourFrom(playerMovement.getWorldCoordinate());

        var getMeMapleLogsDialogue = dialogueManager.builder()
            .npc(
                npcComponent.getNpcDefinition(),
                "Sat in a pub on the Heath..."
            )
            .player("Uhm excuse me?")
            .npc(
                npcComponent.getNpcDefinition(),
                "Oh hi adventurer, sorry I didn't notice you. I have a favour to ask."
            )
            .npc(
                npcComponent.getNpcDefinition(),
                "I really need some Maple Logs. But the only Maple trees on Newbie Island are on the other side of the island to us here. Where the Goblins live. I can't go near those Goblins they would cook me up into a stew straight away."
            )
            .npc(
                npcComponent.getNpcDefinition(),
                "If you can go and collect some Maple Logs for me that would be wonderful. To reward you I will pay 25 Gold Coins for each Maple Log you can provide me."
            )
            .player("Sure");

        var thankYouDialogue = dialogueManager.builder()
            .npc(
                npcComponent.getNpcDefinition(),
                "Thank you adventurer for the Maple Logs."
            );

        playerTask.setCancellablePrimaryTask(
            WalkToTaskStep.worldCoordinate(playerMovement, destinationTile),

            LookAtTaskStep.worldCoordinate(playerMovement, npcComponent.getEntity().getComponentOrThrow(MovementComponent.class).getWorldCoordinate()),

            ImmediateTaskStep.doThis(() -> {
                if (player.getInventory().hasItem(mapleLogsItemDefinition)) {
                    var mapleLogCount = player.getInventory().getQuantityOfItem(mapleLogsItemDefinition);
                    if (player.getInventory().removeItemOfTypeAndQuantity(mapleLogsItemDefinition, mapleLogCount)) {
                        player.getInventory().addItem(goldCoinItemDefinition, mapleLogCount * 25);
                    }
                }
            }),

            dialogueManager.createViewDialogueTaskStep(client, player.getInventory().hasItem(mapleLogsItemDefinition) ? thankYouDialogue : getMeMapleLogsDialogue)
        );
    }
}
