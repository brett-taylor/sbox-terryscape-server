package content.startingzone.worldobjecthandlers;

import com.google.inject.Singleton;
import com.terryscape.cache.world.WorldObjectDefinition;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.TaskStep;
import com.terryscape.game.task.step.impl.ImmediateTaskStep;
import com.terryscape.game.task.step.impl.NextTickTaskStep;
import com.terryscape.game.task.step.impl.WalkToTaskStep;
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
            WalkToTaskStep.worldCoordinate(playerMovement, destination),

            ImmediateTaskStep.doThis(() -> {
                playerChat.sendGameMessage("You begin to chop the %s...".formatted(worldObjectDefinition.getObjectDefinition().getName()));
                playerMovement.look(playerMovement.getWorldCoordinate().directionTo(worldObjectDefinition.getWorldCoordinate()));
            }),

            new FakeChopTreeTaskStep(playerAnimation)
        );
    }

    private static class FakeChopTreeTaskStep extends TaskStep {

        private final AnimationComponent animationComponent;

        private FakeChopTreeTaskStep(AnimationComponent animationComponent) {
            this.animationComponent = animationComponent;
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public void onBecameCurrentTaskStep() {
            super.onBecameCurrentTaskStep();

            animationComponent.playAnimation("Sword_Attack_L3");
        }

        @Override
        public void tick() {
            super.tick();

            animationComponent.playAnimation("Sword_Attack_L3");
        }
    }
}
