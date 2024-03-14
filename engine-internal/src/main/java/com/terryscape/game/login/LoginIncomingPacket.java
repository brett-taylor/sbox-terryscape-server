package com.terryscape.game.login;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.entity.EntityPrefabFactory;
import com.terryscape.game.player.PlayerComponentImpl;
import com.terryscape.net.Client;
import com.terryscape.net.ClientImpl;
import com.terryscape.net.IncomingPacket;
import com.terryscape.world.WorldManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;

@Singleton
public class LoginIncomingPacket implements IncomingPacket {

    private static final Logger LOGGER = LogManager.getLogger(LoginIncomingPacket.class);

    private final WorldManager worldManager;

    private final EntityPrefabFactory entityFactory;

    @Inject
    public LoginIncomingPacket(WorldManager worldManager, EntityPrefabFactory entityFactory) {
        this.worldManager = worldManager;
        this.entityFactory = entityFactory;
    }

    @Override
    public String getPacketName() {
        return "client_server_login";
    }

    @Override
    public void handlePacket(Client client, ByteBuffer packet) {
        var steamId = IncomingPacket.readString(packet).trim();
        var receivedUsername = IncomingPacket.readString(packet).trim();

        var randomPostfix = String.valueOf(Math.random() * 10000).subSequence(0, 3);
        var username = "%s %s".formatted(receivedUsername, randomPostfix);

        LOGGER.info("Login accepted username={}, steamId={}", username, steamId);

        worldManager.sendInitialUpdate(client);

        var playerEntity = entityFactory.createPlayerPrefab();

        var player = playerEntity.getComponentOrThrow(PlayerComponentImpl.class);
        player.setClient(client);
        player.setUsername(username);
        player.setSteamId(steamId);

        var clientImpl = (ClientImpl) client;
        clientImpl.setPlayer(player);

        worldManager.registerEntity(playerEntity);
    }

}
