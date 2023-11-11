package com.terryscape.net.packet.incoming;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.entity.EntityManager;
import com.terryscape.entity.player.PlayerImpl;
import com.terryscape.net.Client;
import com.terryscape.net.ClientImpl;
import com.terryscape.net.packet.IncomingPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;

@Singleton
public class LoginIncomingPacket implements IncomingPacket {

    private static final Logger LOGGER = LogManager.getLogger(LoginIncomingPacket.class);

    private final EntityManager entityManager;

    private final PlayerImpl.PlayerImplFactory playerImplFactory;

    @Inject
    public LoginIncomingPacket(EntityManager entityManager, PlayerImpl.PlayerImplFactory playerImplFactory) {
        this.entityManager = entityManager;
        this.playerImplFactory = playerImplFactory;
    }

    @Override
    public String getPacketName() {
        return "client_server_login";
    }

    @Override
    public void handlePacket(Client client, ByteBuffer packet) {
        var receivedUsername = IncomingPacket.readString(packet).trim();

        var randomPostfix = String.valueOf(Math.random() * 1000).subSequence(0, 3);
        var username = "%s %s".formatted(receivedUsername, randomPostfix);

        LOGGER.info("Login accepted username={}", username);

        var player = playerImplFactory.create(client);
        player.setUsername(username);

        var clientImpl = (ClientImpl) client;
        clientImpl.setPlayer(player);

        entityManager.registerEntity(player);
        entityManager.sendPlayerInitialUpdate(player);
    }

}
