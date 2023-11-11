package com.terryscape.game.chat;

import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

public class GameMessageOutgoingPacket implements OutgoingPacket {

    private String message;
    private String fromUsername;

    public GameMessageOutgoingPacket setMessage(String message) {
        this.message = message;
        return this;
    }

    public GameMessageOutgoingPacket setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
        return this;
    }

    @Override
    public String getPacketName() {
        return "server_client_game_message";
    }

    @Override
    public void writePacket(OutputStream packet) {
        OutgoingPacket.writeString(packet, message);
        OutgoingPacket.writeBoolean(packet, fromUsername != null);

        if (fromUsername != null) {
            OutgoingPacket.writeString(packet, fromUsername);
        }
    }
}
