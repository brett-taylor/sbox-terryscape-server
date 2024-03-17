package content.startingzone.npchandlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.Config;
import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.game.chat.dialogue.DialogueManager;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.game.npc.NpcInteractionHandler;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.WalkToTaskStep;
import com.terryscape.net.Client;
import com.terryscape.entity.EntityManager;
import jakarta.inject.Named;

import java.util.Set;

@Singleton
public class PlayerOnlineCounterNpcInteractionHandler implements NpcInteractionHandler {

    private final DialogueManager dialogueManager;

    private final EntityManager entityManager;

    private final NpcDefinition combatExpertNpcDefinition;

    @Inject
    public PlayerOnlineCounterNpcInteractionHandler(DialogueManager dialogueManager,
                                                    EntityManager entityManager,
                                                    @Named("players_online_counter") NpcDefinition combatExpertNpcDefinition) {

        this.dialogueManager = dialogueManager;
        this.entityManager = entityManager;
        this.combatExpertNpcDefinition = combatExpertNpcDefinition;
    }

    @Override
    public Set<NpcDefinition> getNpcs() {
        return Set.of(combatExpertNpcDefinition);
    }

    @Override
    public void invoke(Client client, NpcComponent npcComponent) {
        var player = client.getPlayer().orElseThrow();
        var playerTask = player.getEntity().getComponentOrThrow(TaskComponent.class);
        var playerMovement = player.getEntity().getComponentOrThrow(MovementComponent.class);

        var playerCount = entityManager.getPlayers().size();
        var message = playerCount == 1
            ? "You are currently the only player on %s.".formatted(Config.NAME)
            : "There are currently %s players on %s.".formatted(playerCount, Config.NAME);

        var dialogue = dialogueManager.builder()
            .npc(npcComponent.getNpcDefinition(), message);

        var npcWorldCoordinate = npcComponent.getEntity().getComponentOrThrow(MovementComponent.class).getWorldCoordinate();
        var destinationTile = npcWorldCoordinate.getClosestCardinalNeighbourFrom(playerMovement.getWorldCoordinate());

        playerTask.setCancellablePrimaryTask(
            WalkToTaskStep.worldCoordinate(playerMovement, destinationTile),
            dialogueManager.createViewDialogueTaskStep(client, dialogue)
        );
    }
}
