package com.terryscape.game.chat;

import com.terryscape.game.player.PlayerComponent;

public interface PlayerChatSystem {

    void sendGameMessage(PlayerComponent playerComponent, String message);

    void handleChat(PlayerComponent playerComponent, String message);

    void sendOverheadText(PlayerComponent playerComponent, String message);

}
