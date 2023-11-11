package com.terryscape.game.login;

import com.terryscape.game.player.PlayerComponent;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

public class SetLocalPlayerOutgoingPacket implements OutgoingPacket {

    private PlayerComponent localPlayer;

    public SetLocalPlayerOutgoingPacket setLocalEntity(PlayerComponent localPlayer) {
        this.localPlayer = localPlayer;
        return this;
    }

    @Override
    public String getPacketName() {
        return "server_client_set_local_player";
    }

    @Override
    public void writePacket(OutputStream packet) {
        OutgoingPacket.writeEntityIdentifier(packet, localPlayer.getEntity().getIdentifier());
    }
}
