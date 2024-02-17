package content.startingzone.worldobjecthandlers;

import com.google.inject.Singleton;
import com.terryscape.cache.world.WorldObjectDefinition;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.Step;
import com.terryscape.game.task.step.impl.ImmediateStep;
import com.terryscape.game.task.step.impl.WalkToStep;
import com.terryscape.game.worldobject.WorldObjectInteractionHandler;
import com.terryscape.net.Client;

import java.util.Set;

@Singleton
public class TreeWorldObjectInteractionHandler implements WorldObjectInteractionHandler {

    @Override
    public Set<String> getObjectIds() {
        return Set.of("sm_env_tree_round_01", "sm_env_tree_round_03");
    }

    @Override
    public void invoke(Client client, WorldObjectDefinition worldObjectDefinition) {
        var player = client.getPlayer().orElseThrow();
        var playerTask = player.getEntity().getComponentOrThrow(TaskComponent.class);
        var playerChat = player.getEntity().getComponentOrThrow(PlayerChatComponent.class);
        var playerMovement = player.getEntity().getComponentOrThrow(MovementComponent.class);
        var playerAnimation = player.getEntity().getComponentOrThrow(AnimationComponent.class);

        var destination = worldObjectDefinition.getWorldCoordinate().getClosestCardinalNeighbourFrom(playerMovement.getWorldCoordinate());

        playerTask.setCancellablePrimaryTask(
            WalkToStep.worldCoordinate(playerMovement, destination),

            ImmediateStep.run(() -> {
                playerChat.sendGameMessage("You begin to chop the %s...".formatted(worldObjectDefinition.getObjectDefinition().getName()));
                playerMovement.look(playerMovement.getWorldCoordinate().directionTo(worldObjectDefinition.getWorldCoordinate()));
            }),

            new FakeChopTreeStep(playerAnimation)
        );
    }

    private static class FakeChopTreeStep extends Step {

        private final AnimationComponent animationComponent;

        private FakeChopTreeStep(AnimationComponent animationComponent) {
            this.animationComponent = animationComponent;
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public void tick() {
            super.tick();

            animationComponent.playAnimation("Sword_Attack_L3");
        }
    }
}
