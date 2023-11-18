package com.terryscape.game.npc;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.movement.MovementComponentImpl;
import com.terryscape.util.RandomUtil;
import com.terryscape.world.Direction;
import com.terryscape.world.WorldCoordinate;

public class WanderMovementComponent extends BaseEntityComponent {

    private MovementComponent movementComponent;

    private WorldCoordinate center;

    private int wanderSize;

    private int tickCountSinceLastWander;

    private int nextWanderTickCount;

    public WanderMovementComponent(Entity entity) {
        super(entity);
    }

    public void startWander(WorldCoordinate center, int wanderSize) {
        this.center = center;
        this.wanderSize = wanderSize;

        movementComponent = getEntity().getComponentOrThrow(MovementComponent.class);
        movementComponent.teleport(randomCoordinateInWanderZone());
        movementComponent.look(Direction.random());
        pickNewRandomInterval();
    }

    @Override
    public void tick() {
        super.tick();

        // TODO Swap to some sort of task system.
        // TODO Should get like some notify callback on the move.

        if (tickCountSinceLastWander >= nextWanderTickCount) {
            movementComponent.move(randomCoordinateInWanderZone());
            tickCountSinceLastWander = 0;
            pickNewRandomInterval();
        } else {
            tickCountSinceLastWander += 1;
        }
    }

    private WorldCoordinate randomCoordinateInWanderZone() {
        var randomX = RandomUtil.randomNumber(-wanderSize, wanderSize);
        var randomY = RandomUtil.randomNumber(-wanderSize, wanderSize);
        return center.add(new WorldCoordinate(randomX, randomY));
    }

    private void pickNewRandomInterval() {
        nextWanderTickCount = RandomUtil.randomNumber(10, 50);
    }
}
