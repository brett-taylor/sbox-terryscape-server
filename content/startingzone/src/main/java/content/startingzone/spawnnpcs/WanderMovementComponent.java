package content.startingzone.spawnnpcs;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.event.type.OnEntityDeathEntityEvent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.task.Task;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.WaitTaskStep;
import com.terryscape.game.task.step.impl.WalkToTaskStep;
import com.terryscape.maths.RandomUtil;
import com.terryscape.world.Direction;
import com.terryscape.world.coordinate.WorldCoordinate;

// TODO: This should move back into engine internal when the npc factory is more complex and can handle conditionally adding components
public class WanderMovementComponent extends BaseEntityComponent {

    private final WorldCoordinate minWanderZone;

    private final WorldCoordinate maxWanderZone;

    private final boolean teleportToSpot;

    private boolean isWandering;

    private MovementComponent movementComponent;

    private TaskComponent taskComponent;

    private Task wanderTask;

    public WanderMovementComponent(Entity entity, WorldCoordinate minWanderZone, WorldCoordinate maxWanderZone, boolean teleportToSpot) {
        super(entity);

        this.minWanderZone = minWanderZone;
        this.maxWanderZone = maxWanderZone;
        this.teleportToSpot = teleportToSpot;

        getEntity().subscribe(OnEntityDeathEntityEvent.class, this::onDeath);
    }

    @Override
    public void onRegistered() {
        super.onRegistered();

        movementComponent = getEntity().getComponentOrThrow(MovementComponent.class);
        taskComponent = getEntity().getComponentOrThrow(TaskComponent.class);

        isWandering = true;

        if (teleportToSpot) {
            movementComponent.teleport(randomCoordinateInWanderZone());
            movementComponent.look(Direction.random());
        }
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
            WaitTaskStep.ticks(randomWaitInterval()),
            WalkToTaskStep.worldCoordinate(movementComponent, randomCoordinateInWanderZone())
        );

        if (wanderTaskOptional.isPresent()) {
            wanderTask = wanderTaskOptional.get();
            wanderTask.onFinished(ignored -> wanderTask = null);
        }
    }

    private WorldCoordinate randomCoordinateInWanderZone() {
        var randomX = RandomUtil.randomNumber(minWanderZone.getX(), maxWanderZone.getX());
        var randomY = RandomUtil.randomNumber(minWanderZone.getY(), maxWanderZone.getY());
        return new WorldCoordinate(randomX, randomY);
    }

    private int randomWaitInterval() {
        return RandomUtil.randomNumber(5, 80);
    }

    private void onDeath(OnEntityDeathEntityEvent onEntityDeathEntityEvent) {
        stopWander();
    }
}
