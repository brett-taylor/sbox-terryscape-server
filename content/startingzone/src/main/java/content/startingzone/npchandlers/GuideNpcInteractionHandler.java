package content.startingzone.npchandlers;

import com.google.inject.Singleton;
import com.terryscape.Config;
import com.terryscape.game.chat.dialogue.PlayerDialogueComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.game.npc.NpcInteractionHandler;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.WalkToTaskStep;
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
            .npc(npcComponent.getNpcDefinition(), "Welcome to %s! A land of monsters and dangers. But a world where...".formatted(Config.NAME))
            .player("Uh, why are are not wearing any clothes?")
            .npc(npcComponent.getNpcDefinition(), "It doesn't matter!")
            .npc(npcComponent.getNpcDefinition(), "To get started in %s, firstly you need some equipment.".formatted(Config.NAME))
            .npc(npcComponent.getNpcDefinition(), "You see that shop over there?")
            .player("Yeah?")
            .npc(npcComponent.
                    getNpcDefinition(),
                " The couple that runs the place. They aren't really with it anymore. They leave all their gold on the table in the middle of the place."
            )
            .npc(
                npcComponent.getNpcDefinition(),
                "You're not really stealing the gold if you instantly give it back to them. You should take some and use it to buy equipment from them."
            )
            .npc(
                npcComponent.getNpcDefinition(),
                "To make it up to them. Maybe you could try clearing out the Goblin infestation near the shop for them."
            )
            .npc(
                npcComponent.getNpcDefinition(),
                "Oh and remember, Terry has recently made killing other people completely legal - so watch yourself out there."
            );

        playerTask.setCancellablePrimaryTask(
            WalkToTaskStep.worldCoordinate(playerMovement, destinationTile),

            playerDialogue.createViewDialogueTaskStep(dialogue)
        );
    }
}
