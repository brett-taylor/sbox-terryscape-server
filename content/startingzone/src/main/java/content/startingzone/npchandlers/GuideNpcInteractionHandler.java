package content.startingzone.npchandlers;

import com.google.inject.Singleton;
import com.terryscape.Config;
import com.terryscape.game.chat.dialogue.PlayerDialogueComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.game.npc.NpcInteractionHandler;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.ImmediateStep;
import com.terryscape.game.task.step.impl.WalkToStep;
import com.terryscape.net.Client;

import java.util.Set;

@Singleton
public class GuideNpcInteractionHandler implements NpcInteractionHandler {

    @Override
    public Set<String> getNpcIds() {
        return Set.of("guide");
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
            .npc(npcComponent.getNpcDefinition().getName(), "Welcome to %s! A land of monsters and dangers. But a world where...".formatted(Config.NAME))
            .player("Uh, why are are not wearing any clothes?");

        playerTask.setCancellablePrimaryTask(
            WalkToStep.worldCoordinate(playerMovement, destinationTile),
            ImmediateStep.run(() -> playerDialogue.start(dialogue))
        );
    }
}
