package com.terryscape.game.movement;

import com.google.inject.Inject;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.net.OutgoingPacket;
import com.terryscape.world.Direction;
import com.terryscape.world.WorldCoordinate;
import com.terryscape.world.pathfinding.PathfindingManager;
import com.terryscape.world.pathfinding.PathfindingRoute;

import java.io.OutputStream;

public class PlayerMovementComponentImpl extends BaseEntityComponent implements PlayerMovementComponent {

    private final PathfindingManager pathfindingManager;

    private WorldCoordinate worldCoordinate;

    private Direction direction;

    private PathfindingRoute pathfindingRoute;

    private boolean nextUpdateWasTeleport = false;

    @Inject
    public PlayerMovementComponentImpl(Entity entity, PathfindingManager pathfindingManager) {
        super(entity);

        this.pathfindingManager = pathfindingManager;
    }

    @Override
    public String getComponentIdentifier() {
        return "component_player_movement";
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
    public void onAdded() {
        super.onAdded();

        teleport(new WorldCoordinate(5, 0));
        look(Direction.NORTH);
    }

    @Override
    public void look(Direction direction) {
        this.direction = direction;
        stop();
    }

    @Override
    public void teleport(WorldCoordinate destination) {
        this.worldCoordinate = destination;

        stop();

        nextUpdateWasTeleport = true;
    }

    @Override
    public void move(WorldCoordinate destination) {
        var result = this.pathfindingManager.findRoute(getWorldCoordinate(), destination);

        if (result.isEmpty()) {
            return;
        }

        pathfindingRoute = result.get();
    }

    @Override
    public void stop() {
        pathfindingRoute = null;
    }

    public void tick() {
        if (pathfindingRoute == null) {
            return;
        }

        if (!pathfindingRoute.hasNextTile()) {
            pathfindingRoute = null;
            return;
        }

        var newTile = pathfindingRoute.getNextTile();
        var newDirection = getWorldCoordinate().directionTo(newTile);

        this.worldCoordinate = newTile;
        this.direction = newDirection;
    }

    @Override
    public void writeEntityAddedPacket(OutputStream packet) {
        OutgoingPacket.writeWorldCoordinate(packet, getWorldCoordinate());
        OutgoingPacket.writeDirection(packet, getDirection());
    }

    @Override
    public void writeEntityUpdatedPacket(OutputStream packet) {
        OutgoingPacket.writeWorldCoordinate(packet, getWorldCoordinate());
        OutgoingPacket.writeBoolean(packet, nextUpdateWasTeleport);
        OutgoingPacket.writeDirection(packet, getDirection());

        nextUpdateWasTeleport = false;
    }

}
