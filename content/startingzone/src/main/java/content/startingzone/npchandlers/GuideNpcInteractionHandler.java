package content.startingzone.npchandlers;

import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.game.npc.NpcInteractionHandler;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.ImmediateStep;
import com.terryscape.game.task.step.impl.WalkToStep;
import com.terryscape.net.Client;

import java.util.Set;

public class GuideNpcInteractionHandler implements NpcInteractionHandler {
    @Override
    public Set<String> getNpcIds() {
        return Set.of("guide");
    }

    @Override
    public void invoke(Client client, NpcComponent npcComponent) {
        var player = client.getPlayer().orElseThrow();
        var playerTask = player.getEntity().getComponentOrThrow(TaskComponent.class);
        var playerChat = player.getEntity().getComponentOrThrow(PlayerChatComponent.class);
        var playerMovement = player.getEntity().getComponentOrThrow(MovementComponent.class);

        var npcWorldCoordinate = npcComponent.getEntity().getComponentOrThrow(MovementComponent.class).getWorldCoordinate();
        var destinationTile = npcWorldCoordinate.getClosestCardinalNeighbourFrom(playerMovement.getWorldCoordinate());

        playerTask.setCancellablePrimaryTask(
            WalkToStep.worldCoordinate(playerMovement, destinationTile),
            ImmediateStep.run(() -> playerChat.sendGameMessage("The Guide seems to ignore you..."))
        );
    }
}
