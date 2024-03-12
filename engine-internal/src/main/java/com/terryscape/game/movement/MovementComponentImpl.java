package com.terryscape.game.movement;

import com.google.inject.Inject;
import com.terryscape.entity.Entity;
import com.terryscape.entity.EntityIdentifier;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.event.type.OnDeathEntityEvent;
import com.terryscape.net.OutgoingPacket;
import com.terryscape.world.Direction;
import com.terryscape.world.coordinate.WorldCoordinate;
import com.terryscape.world.pathfinding.PathfindingManager;
import com.terryscape.world.pathfinding.PathfindingRoute;

import java.io.OutputStream;

public class MovementComponentImpl extends BaseEntityComponent implements MovementComponent {

    private final PathfindingManager pathfindingManager;

    private WorldCoordinate worldCoordinate = new WorldCoordinate(0, 0);

    private Direction direction = Direction.NORTH;

    private WorldCoordinate lerpWorldCoordinate;

    private Direction lerpDirection;

    private MovementComponent facing;

    private PathfindingRoute pathfindingRoute;

    private boolean nextUpdateWasTeleport = false;

    private MovementSpeed movementSpeed = MovementSpeed.WALK;

    private boolean hasMovedThisTick = false;

    @Inject
    public MovementComponentImpl(Entity entity, PathfindingManager pathfindingManager) {
        super(entity);

        this.pathfindingManager = pathfindingManager;

        getEntity().subscribe(OnDeathEntityEvent.class, this::onDeath);
    }

    @Override
    public String getComponentIdentifier() {
        return "component_movement";
    }

    @Override
    public WorldCoordinate getWorldCoordinate() {
        return worldCoordinate;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setMovementSpeed(MovementSpeed movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    @Override
    public void look(Direction direction) {
        this.direction = direction;
        stop();
    }

    @Override
    public void face(MovementComponent movementComponentToFace) {
        facing = movementComponentToFace;
    }

    @Override
    public void teleport(WorldCoordinate destination) {
        this.worldCoordinate = destination;

        stop();

        nextUpdateWasTeleport = true;
    }

    @Override
    public boolean move(WorldCoordinate destination) {
        var result = this.pathfindingManager.findRoute(getWorldCoordinate(), destination);
        if (result.isEmpty()) {
            return false;
        }

        pathfindingRoute = result.get();

        if (!hasMovedThisTick) {
            tick();
        }

        return true;
    }

    @Override
    public void stop() {
        pathfindingRoute = null;
    }

    @Override
    public void stopFacing() {
        facing = null;
    }

    public void tick() {
        if (hasMovedThisTick) {
            return;
        }

        if (facing != null) {
            this.direction = getWorldCoordinate().directionTo(facing.getWorldCoordinate());
        }

        if (pathfindingRoute == null) {
            return;
        }

        if (!pathfindingRoute.hasNextWorldCoordinate()) {
            pathfindingRoute = null;
            return;
        }

        if (movementSpeed == MovementSpeed.RUN && pathfindingRoute.remaining() >= 2) {
            lerpWorldCoordinate = pathfindingRoute.getNextWorldCoordinate();
            lerpDirection = getWorldCoordinate().directionTo(lerpWorldCoordinate);
        } else {
            lerpWorldCoordinate = null;
            lerpDirection = null;
        }

        WorldCoordinate newDestinationTile = pathfindingRoute.getNextWorldCoordinate();

        Direction newDirection;
        if (lerpWorldCoordinate != null) {
            newDirection = lerpWorldCoordinate.directionTo(newDestinationTile);
        } else {
            newDirection = getWorldCoordinate().directionTo(newDestinationTile);
        }

        this.worldCoordinate = newDestinationTile;
        setDirectionIfNotFacing(newDirection);

        hasMovedThisTick = true;
    }

    @Override
    public void writeEntityAddedPacket(OutputStream packet) {
        getWorldCoordinate().writeToPacket(packet);
        Direction.writeToPacket(packet, getDirection());
    }

    @Override
    public void writeEntityUpdatedPacket(OutputStream packet) {
        getWorldCoordinate().writeToPacket(packet);
        OutgoingPacket.writeBoolean(packet, nextUpdateWasTeleport);
        Direction.writeToPacket(packet, getDirection());

        if (facing == null) {
            EntityIdentifier.writeToPacketNullIdentifier(packet);
        } else {
            facing.getEntity().getIdentifier().writeToPacket(packet);
        }

        OutgoingPacket.writeBoolean(packet, lerpWorldCoordinate != null);
        if (lerpWorldCoordinate != null) {
            lerpWorldCoordinate.writeToPacket(packet);
            Direction.writeToPacket(packet, lerpDirection);
        }

        nextUpdateWasTeleport = false;
        hasMovedThisTick = false;
    }

    private void onDeath(OnDeathEntityEvent onDeathEntityEvent) {
        stop();
    }

    private void setDirectionIfNotFacing(Direction newDirection) {
        if (facing == null) {
            this.direction = newDirection;
        }
    }

}
