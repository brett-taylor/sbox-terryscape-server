package com.terryscape.game.chat.command;

import java.util.Arrays;
import java.util.List;

public class CommandDescription {

    public static CommandDescription of(String description, CommandArgumentDescription... argumentDescriptions) {
        return new CommandDescription(description, Arrays.stream(argumentDescriptions).toList());
    }

    private final String description;

    private final List<CommandArgumentDescription> argumentDescriptions;

    private CommandDescription(String description, List<CommandArgumentDescription> argumentDescriptions) {
        this.description = description;
        this.argumentDescriptions = argumentDescriptions;
    }

    public String getDescription() {
        return description;
    }

    public List<CommandArgumentDescription> getArgumentDescriptions() {
        return argumentDescriptions;
    }
}
