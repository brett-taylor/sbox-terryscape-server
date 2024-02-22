package content.startingzone.npchandlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.game.chat.dialogue.PlayerDialogueComponent;
import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.game.npc.NpcInteractionHandler;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.ImmediateTaskStep;
import com.terryscape.game.task.step.impl.WalkToTaskStep;
import com.terryscape.net.Client;

import java.util.Set;

@Singleton
public class ShopKeeperNpcInteractionHandler implements NpcInteractionHandler {

    private final InterfaceManager interfaceManager;

    @Inject
    public ShopKeeperNpcInteractionHandler(InterfaceManager interfaceManager) {
        this.interfaceManager = interfaceManager;
    }

    @Override
    public Set<String> getNpcIds() {
        return Set.of("shop_keeper");
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
            .player("Hi, I would like to see your stock please.")
            .npc(npcComponent.getNpcDefinition(), "Certainly.");

        playerTask.setCancellablePrimaryTask(
            WalkToTaskStep.worldCoordinate(playerMovement, destinationTile),

            playerDialogue.createDialogueTaskStep(dialogue),

            ImmediateTaskStep.doThis(() -> interfaceManager.showInterface(client, "welcome_screen"))
        );
    }
}
