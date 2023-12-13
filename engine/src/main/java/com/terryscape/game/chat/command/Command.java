package com.terryscape.game.chat.command;

import com.terryscape.game.player.PlayerComponent;

import java.util.List;

public interface Command {

    String getPhrase();

    CommandDescription getDescription();

    void execute(PlayerComponent playerComponent, List<String> arguments);
}
