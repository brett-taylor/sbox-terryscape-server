package com.terryscape.net;

import com.terryscape.game.player.PlayerComponent;
import org.java_websocket.WebSocket;

import java.util.Optional;

public interface Client {

    WebSocket getConnection();

    Optional<PlayerComponent> getPlayer();
}
