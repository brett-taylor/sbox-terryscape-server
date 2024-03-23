package com.terryscape.game.chat;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.net.Client;
import com.terryscape.net.IncomingPacket;

import java.nio.ByteBuffer;

@Singleton
public class PlayerChatIncomingPacket implements IncomingPacket {

    private final PlayerChatSystem playerChatSystem;

    @Inject
    public PlayerChatIncomingPacket(PlayerChatSystem playerChatSystem) {
        this.playerChatSystem = playerChatSystem;
    }

    @Override
    public String getPacketName() {
        return "client_server_player_chat";
    }

    @Override
    public void handlePacket(Client client, ByteBuffer packet) {
        var message = IncomingPacket.readString(packet);
        var player = client.getPlayer().orElseThrow();

        playerChatSystem.handleChat(player, message);
    }
}
