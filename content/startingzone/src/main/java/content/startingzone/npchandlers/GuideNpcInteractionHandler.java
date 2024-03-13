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
public class GuideNpcInteractionHandler implements NpcInteractionHandler {

    private final DialogueManager dialogueManager;

    @Inject
    public GuideNpcInteractionHandler(DialogueManager dialogueManager) {
        this.dialogueManager = dialogueManager;
    }

    @Override
    public Set<String> getNpcIds() {
        return Set.of("guide");
    }

    @Override
    public void invoke(Client client, NpcComponent npcComponent) {
        var player = client.getPlayer().orElseThrow();
        var playerTask = player.getEntity().getComponentOrThrow(TaskComponent.class);
        var playerMovement = player.getEntity().getComponentOrThrow(MovementComponent.class);

        var npcWorldCoordinate = npcComponent.getEntity().getComponentOrThrow(MovementComponent.class).getWorldCoordinate();
        var destinationTile = npcWorldCoordinate.getClosestCardinalNeighbourFrom(playerMovement.getWorldCoordinate());

        var dialogue = dialogueManager.builder()
            .npc(
                npcComponent.getNpcDefinition(),
                "Welcome to %s, %s!".formatted(NAME, player.getUsername())
            )
            .player("Hi. What am I meant to be doing here?")
            .npc(
                npcComponent.getNpcDefinition(),
                "Why don't you attempt to buy some items from the shops over there. They have equipment for both warriors and wizards currently. I hear they will be getting equipment for rangers soon."
            )
            .npc(
                npcComponent.getNpcDefinition(),
                "You can earn money from either stealing from that table with coins on it or by defeating Goblins."
            )
            .npc(
                npcComponent.getNpcDefinition(),
                "I would only attack the little Goblins right now. The Warriors can put up quite a fight. I wouldn't even look at the Chief unless you have a group of fighters like yourself ready."
            )
            .npc(
                npcComponent.getNpcDefinition(),
                "The Weapon Shop Keeper also sells Godswords. Their special attack has quite a punch and would be very useful when fighting other players."
            )
            .npc(
                npcComponent.getNpcDefinition(),
                "Remember when you're feeling low on health to eat some food, that will make you feel much better. You can get food from the Food Shop Keeper."
            )
            .blank(
                "%s is currently in a Pre-Alpha Test. Your progress will not be saved once you exit. There are cheats you can use to help you progress. You can see these by writing ::help in your Chatbox.".formatted(NAME)
            )
            .blank(
                "Your feedback will be greatly appreciated. Please pass all your feedback and ideas onto Waisie Milliams in the SBox Discord. Please also remember to report any bug or unexpected behaviour you come across!"
            )
            .blank(
                "Thank you for trying out %s.".formatted(NAME)
            );

        playerTask.setCancellablePrimaryTask(
            WalkToTaskStep.worldCoordinate(playerMovement, destinationTile),

            dialogueManager.createViewDialogueTaskStep(client, dialogue)
        );
    }
}
