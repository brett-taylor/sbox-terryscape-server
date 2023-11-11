package com.terryscape.game.chat;

import com.google.inject.Singleton;
import com.terryscape.net.Client;
import com.terryscape.net.IncomingPacket;

import java.nio.ByteBuffer;

@Singleton
public class PlayerChatIncomingPacket implements IncomingPacket {

    @Override
    public String getPacketName() {
        return "client_server_player_chat";
    }

    @Override
    public void handlePacket(Client client, ByteBuffer packet) {
        var message = IncomingPacket.readString(packet);
        var player = client.getPlayer().orElseThrow();
        player.getEntity().getComponentOrThrow(PlayerChatComponent.class).handleChat(message);
    }
}
