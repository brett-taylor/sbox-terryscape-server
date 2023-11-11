package com.terryscape.net;

import com.terryscape.entity.player.Player;
import org.java_websocket.WebSocket;

import java.util.Optional;

public class ClientImpl implements Client {

    private final WebSocket connection;

    private Player player;

    public ClientImpl(WebSocket connection) {
        this.connection = connection;
    }

    @Override
    public WebSocket getConnection() {
        return connection;
    }

    @Override
    public Optional<Player> getPlayer() {
        return Optional.of(player);
    }

    public ClientImpl setPlayer(Player player) {
        this.player = player;
        return this;
    }
}
