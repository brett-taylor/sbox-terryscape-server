package com.terryscape.net;

import com.terryscape.entity.player.Player;
import org.java_websocket.WebSocket;

import java.util.Optional;

public interface Client {

    WebSocket getConnection();

    Optional<Player> getPlayer();
}
