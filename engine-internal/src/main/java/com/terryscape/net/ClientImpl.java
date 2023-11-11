package com.terryscape.net;

import com.terryscape.game.player.PlayerComponent;
import org.java_websocket.WebSocket;

import java.util.Optional;

public class ClientImpl implements Client {

    private final WebSocket connection;

    private PlayerComponent player;

    public ClientImpl(WebSocket connection) {
        this.connection = connection;
    }

    @Override
    public WebSocket getConnection() {
        return connection;
    }

    @Override
    public Optional<PlayerComponent> getPlayer() {
        return Optional.of(player);
    }

    public ClientImpl setPlayer(PlayerComponent player) {
        this.player = player;
        return this;
    }
}
