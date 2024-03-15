package content.startingzone.npchandlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.game.chat.dialogue.DialogueManager;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.game.npc.NpcInteractionHandler;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.WalkToTaskStep;
import com.terryscape.net.Client;

import java.util.Set;

import static com.terryscape.Config.NAME;

@Singleton
public class CombatExpertNpcInteractionHandler implements NpcInteractionHandler {

    private final DialogueManager dialogueManager;

    @Inject
    public CombatExpertNpcInteractionHandler(DialogueManager dialogueManager) {
        this.dialogueManager = dialogueManager;
    }

    @Override
    public Set<String> getNpcIds() {
        return Set.of("combat_expert");
    }

    @Override
    public void invoke(Client client, NpcComponent npcComponent) {
        var player = client.getPlayer().orElseThrow();
        var playerTask = player.getEntity().getComponentOrThrow(TaskComponent.class);
        var playerMovement = player.getEntity().getComponentOrThrow(MovementComponent.class);

        var npcWorldCoordinate = npcComponent.getEntity().getComponentOrThrow(MovementComponent.class).getWorldCoordinate();
        var destinationTile = npcWorldCoordinate.getClosestCardinalNeighbourFrom(playerMovement.getWorldCoordinate());

        var dialogue = dialogueManager.builder()
            .player("Hi can you explain combat to me?")
            .npc(
                npcComponent.getNpcDefinition(),
                "Sure combat is all about what method you're using and what items you're wearing."
            )
            .npc(
                npcComponent.getNpcDefinition(),
                "There are three methods of combat: Melee, Range, and Magic. Melee is all about being close to your enemy, while Range and Magic is about keeping that distance."
            )
            .npc(
                npcComponent.getNpcDefinition(),
                "You can also dual wield weapons, some weapons though require your full focus and can not be duel wielded."
            )
            .blank(
                "You can dual wield weapons by right clicking the weapon you wish to equip and choose \"Off-hand\"."
            )
            .npc(
                npcComponent.getNpcDefinition(),
                "Some weapons even have a special attack. I believe they are called Godswords. If you get one of those remember to use its special attack as that what it excels at."
            )
            .blank(
                "If you have a weapon with a special attack, you can enable it using the \"Sp\" orb in the top right of your screen."
            )
            .npc(
                npcComponent.getNpcDefinition(),
                "That's all the information I have. Although I am called an expert here on Newbie Island I am not. The warriors on the mainland are much more impressive."
            );

        playerTask.setCancellablePrimaryTask(
            WalkToTaskStep.worldCoordinate(playerMovement, destinationTile),

            dialogueManager.createViewDialogueTaskStep(client, dialogue)
        );
    }
}
