package com.terryscape.net.packet.incoming;

import com.google.inject.Singleton;
import com.terryscape.net.Client;
import com.terryscape.net.packet.IncomingPacket;

import java.nio.ByteBuffer;

@Singleton
public class WalkIncomingPacket implements IncomingPacket {
    @Override
    public String getPacketName() {
        return "client_server_walk";
    }

    @Override
    public void handlePacket(Client client, ByteBuffer packet) {
        var destinationTile = IncomingPacket.readWorldCoordinate(packet);
        var teleport = IncomingPacket.readBoolean(packet);

        // TODO Check is valid tile?

        var player = client.getPlayer().orElseThrow();

        if (teleport) {
            player.getPlayerMovement().teleport(destinationTile);
        } else {
            player.getPlayerMovement().move(destinationTile);
        }
    }
}
