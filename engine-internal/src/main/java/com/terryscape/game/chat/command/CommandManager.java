package com.terryscape.game.chat.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.game.player.PlayerComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

// TODO: Validate arguments before executing and pass them in as their correct types
// TODO: Validate optional parameters they should appear last

@Singleton
public class CommandManager {

    private static final Logger LOGGER = LogManager.getLogger(CommandManager.class);

    public static final String COMMAND_PREFIX = "::";

    private final Map<String, Command> commands;

    @Inject
    public CommandManager(Set<Command> possibleCommands) {
        this.commands = createCommands(possibleCommands);
    }

    public boolean checkForCommandPhaseAndExecuteIfFound(PlayerComponent player, String phase) {
        if (!phase.startsWith(COMMAND_PREFIX)) {
            return false;
        }

        var phraseParts = Arrays.stream(phase.split(" ")).toList();
        if (phraseParts.isEmpty()) {
            return false;
        }

        var commandPhrase = phraseParts.get(0).toLowerCase().replace(COMMAND_PREFIX, "");
        if (!commands.containsKey(commandPhrase)) {
            return false;
        }

        var command = commands.get(commandPhrase);
        var arguments = new ArrayList<>(phraseParts);
        arguments.remove(0);

        LOGGER.info("Player {} executed command {} ({})", player.getUsername(), command.getPhrase(), phase);

        command.execute(player, arguments);
        return true;
    }

    private Map<String, Command> createCommands(Set<Command> possibleCommands) {
        var mapToReturn = possibleCommands.stream().collect(Collectors.toMap(
            command -> command.getPhrase().toLowerCase(),
            Function.identity()
        ));

        var helpCommand = new HelpCommand(mapToReturn.values());
        mapToReturn.put(helpCommand.getPhrase(), helpCommand);

        return mapToReturn;
    }
}
