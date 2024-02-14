package content.devtools.testnps;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.event.type.OnEntityDeathEntityEvent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.task.Task;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.WaitStep;
import com.terryscape.game.task.step.impl.WalkToStep;
import com.terryscape.maths.RandomUtil;
import com.terryscape.world.Direction;
import com.terryscape.world.coordinate.WorldCoordinate;

// TODO: This should move back into engine internal when the npc factory is more complex and can handle conditionally adding components

public class WanderMovementComponent extends BaseEntityComponent {

    private final int wanderSize;

    private WorldCoordinate wanderCenter;

    private boolean isWandering;

    private MovementComponent movementComponent;

    private TaskComponent taskComponent;

    private Task wanderTask;

    public WanderMovementComponent(Entity entity, int wanderSize) {
        super(entity);

        this.wanderSize = wanderSize;

        getEntity().subscribe(OnEntityDeathEntityEvent.class, this::onDeath);
    }

    @Override
    public void onRegistered() {
        super.onRegistered();

        movementComponent = getEntity().getComponentOrThrow(MovementComponent.class);
        taskComponent = getEntity().getComponentOrThrow(TaskComponent.class);

        isWandering = true;

        wanderCenter = movementComponent.getWorldCoordinate();
        movementComponent.teleport(randomCoordinateInWanderZone());
        movementComponent.look(Direction.random());
    }

    public void stopWander() {
        if (wanderTask != null) {
            wanderTask.cancel();
        }

        isWandering = false;
    }

    @Override
    public void tick() {
        super.tick();

        if (!isWandering || wanderTask != null) {
            return;
        }

        createWanderTask();
    }

    private void createWanderTask() {
        if (taskComponent.hasPrimaryTask()) {
            return;
        }

        var wanderTaskOptional = taskComponent.setCancellablePrimaryTask(
            WaitStep.ticks(randomWaitInterval()),
            WalkToStep.worldCoordinate(movementComponent, randomCoordinateInWanderZone())
        );

        if (wanderTaskOptional.isPresent()) {
            wanderTask = wanderTaskOptional.get();
            wanderTask.onFinished(ignored -> wanderTask = null);
        }
    }

    private WorldCoordinate randomCoordinateInWanderZone() {
        var randomX = RandomUtil.randomNumber(-wanderSize, wanderSize);
        var randomY = RandomUtil.randomNumber(-wanderSize, wanderSize);
        return wanderCenter.add(new WorldCoordinate(randomX, randomY));
    }

    private int randomWaitInterval() {
        return RandomUtil.randomNumber(5, 80);
    }

    private void onDeath(OnEntityDeathEntityEvent onEntityDeathEntityEvent) {
        stopWander();
    }
}
