package com.terryscape.game.login;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.entity.EntityManagerImpl;
import com.terryscape.game.player.PlayerComponentImpl;
import com.terryscape.game.player.PlayerFactory;
import com.terryscape.net.Client;
import com.terryscape.net.ClientImpl;
import com.terryscape.net.IncomingPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;

@Singleton
public class LoginIncomingPacket implements IncomingPacket {

    private static final Logger LOGGER = LogManager.getLogger(LoginIncomingPacket.class);

    private final EntityManagerImpl entityManager;

    private final PlayerFactory playerFactory;

    @Inject
    public LoginIncomingPacket(EntityManagerImpl entityManager, PlayerFactory playerFactory) {
        this.entityManager = entityManager;
        this.playerFactory = playerFactory;
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

        entityManager.sendInitialUpdate(client);

        var playerEntity = playerFactory.createUnregisteredEntityWithAllNecessaryPlayerComponents();

        var player = playerEntity.getComponentOrThrow(PlayerComponentImpl.class);
        player.setClient(client);
        player.setUsername(username);

        var clientImpl = (ClientImpl) client;
        clientImpl.setPlayer(player);

        entityManager.registerEntity(playerEntity);
    }

}
