package com.terryscape.game.chat;

import com.terryscape.entity.component.EntityComponent;

public interface PlayerChatComponent extends EntityComponent {

    void sendGameMessage(String message);

    void handleChat(String message);

    void sendOverheadText(String message);
}
