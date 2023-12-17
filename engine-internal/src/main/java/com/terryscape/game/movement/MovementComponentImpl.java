package com.terryscape.game.movement;

import com.google.inject.Inject;
import com.terryscape.entity.Entity;
import com.terryscape.entity.EntityIdentifier;
import com.terryscape.entity.event.type.OnEntityDeathEntityEvent;
import com.terryscape.game.BaseEntityComponentImpl;
import com.terryscape.game.combat.health.HealthComponentImpl;
import com.terryscape.net.OutgoingPacket;
import com.terryscape.world.Direction;
import com.terryscape.world.WorldCoordinate;
import com.terryscape.world.pathfinding.PathfindingManager;
import com.terryscape.world.pathfinding.PathfindingRoute;

import java.io.OutputStream;

public class MovementComponentImpl extends BaseEntityComponentImpl implements MovementComponent {

    private final PathfindingManager pathfindingManager;

    private WorldCoordinate worldCoordinate = new WorldCoordinate(0, 0);

    private Direction direction = Direction.NORTH;

    private MovementComponent facing;

    private PathfindingRoute pathfindingRoute;

    private boolean nextUpdateWasTeleport = false;

    private MovementSpeed movementSpeed = MovementSpeed.WALK;

    @Inject
    public MovementComponentImpl(Entity entity, PathfindingManager pathfindingManager) {
        super(entity);

        this.pathfindingManager = pathfindingManager;

        subscribe(OnEntityDeathEntityEvent.class, "onDeath");
    }

    @Override
    public String getComponentIdentifier() {
        return "component_movement_component";
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

        WorldCoordinate newDestinationTile;
        if (movementSpeed == MovementSpeed.RUN && pathfindingRoute.remaining() >= 2) {
            pathfindingRoute.getNextWorldCoordinate();
            newDestinationTile = pathfindingRoute.getNextWorldCoordinate();
        } else {
            newDestinationTile = pathfindingRoute.getNextWorldCoordinate();
        }

        var newDirection = getWorldCoordinate().directionTo(newDestinationTile);
        this.worldCoordinate = newDestinationTile;
        setDirectionIfNotFacing(newDirection);
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

        nextUpdateWasTeleport = false;
    }

    private void onDeath(OnEntityDeathEntityEvent onEntityDeathEntityEvent) {
        stop();
    }

    private void setDirectionIfNotFacing(Direction newDirection) {
        if (facing == null) {
            this.direction = newDirection;
        }
    }
}
