package com.terryscape.game.movement;

import com.google.inject.Inject;
import com.terryscape.net.packet.OutgoingPacket;
import com.terryscape.world.Direction;
import com.terryscape.world.WorldCoordinate;
import com.terryscape.world.pathfinding.PathfindingManager;
import com.terryscape.world.pathfinding.PathfindingRoute;

import java.io.OutputStream;

public class PlayerMovementImpl implements PlayerMovement {

    public interface PlayerMovementImplFactory {
        PlayerMovementImpl create();
    }

    private final PathfindingManager pathfindingManager;

    private WorldCoordinate worldCoordinate;

    private Direction direction;

    private PathfindingRoute pathfindingRoute;

    private boolean nextUpdateWasTeleport = false;

    @Inject
    public PlayerMovementImpl(PathfindingManager pathfindingManager) {
        this.pathfindingManager = pathfindingManager;
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

    public void writeEntityAddedPacket(OutputStream packet) {
        OutgoingPacket.writeWorldCoordinate(packet, getWorldCoordinate());
        OutgoingPacket.writeDirection(packet, getDirection());
    }

    public void writeEntityUpdatedPacket(OutputStream packet) {
        OutgoingPacket.writeWorldCoordinate(packet, getWorldCoordinate());
        OutgoingPacket.writeBoolean(packet, nextUpdateWasTeleport);
        OutgoingPacket.writeDirection(packet, getDirection());

        nextUpdateWasTeleport = false;
    }
}
