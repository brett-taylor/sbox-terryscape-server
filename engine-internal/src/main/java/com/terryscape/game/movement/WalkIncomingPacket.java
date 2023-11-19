package com.terryscape.game.movement;

import com.google.inject.Singleton;
import com.terryscape.net.Client;
import com.terryscape.net.IncomingPacket;
import com.terryscape.world.WorldCoordinate;

import java.nio.ByteBuffer;

@Singleton
public class WalkIncomingPacket implements IncomingPacket {
    @Override
    public String getPacketName() {
        return "client_server_walk";
    }

    @Override
    public void handlePacket(Client client, ByteBuffer packet) {
        var destinationTile = WorldCoordinate.readFromPacket(packet);
        var teleport = IncomingPacket.readBoolean(packet);

        // TODO Check is valid tile?

        var player = client.getPlayer().orElseThrow();
        var playerMovementComponent = player.getEntity().getComponentOrThrow(MovementComponent.class);

        if (teleport) {
            playerMovementComponent.teleport(destinationTile);
        } else {
            playerMovementComponent.move(destinationTile);
        }
    }
}
