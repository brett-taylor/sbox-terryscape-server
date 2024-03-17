package content.startingzone.npchandlers.quests.gather;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.game.chat.dialogue.DialogueManager;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.game.npc.NpcInteractionHandler;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.WalkToTaskStep;
import com.terryscape.net.Client;

import java.util.Set;

@Singleton
public class PetersNpcInteractionHandler implements NpcInteractionHandler {

    private final DialogueManager dialogueManager;

    private final NpcDefinition peterNpcDefinition;

    @Inject
    public PetersNpcInteractionHandler(DialogueManager dialogueManager, @Named("peters") NpcDefinition peterNpcDefinition) {
        this.dialogueManager = dialogueManager;
        this.peterNpcDefinition = peterNpcDefinition;
    }

    @Override
    public Set<NpcDefinition> getNpcs() {
        return Set.of(peterNpcDefinition);
    }

    @Override
    public void invoke(Client client, NpcComponent npcComponent) {
        var player = client.getPlayer().orElseThrow();
        var playerTask = player.getEntity().getComponentOrThrow(TaskComponent.class);
        var playerMovement = player.getEntity().getComponentOrThrow(MovementComponent.class);

        var npcWorldCoordinate = npcComponent.getEntity().getComponentOrThrow(MovementComponent.class).getWorldCoordinate();
        var destinationTile = npcWorldCoordinate.getClosestCardinalNeighbourFrom(playerMovement.getWorldCoordinate());

        var dialogue = dialogueManager.builder().blank("Peters seems to ignore you...");

        playerTask.setCancellablePrimaryTask(
            WalkToTaskStep.worldCoordinate(playerMovement, destinationTile),

            dialogueManager.createViewDialogueTaskStep(client, dialogue)
        );
    }
}
