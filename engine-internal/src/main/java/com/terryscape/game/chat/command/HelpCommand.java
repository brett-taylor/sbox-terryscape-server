package com.terryscape.game.chat.command;

import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.player.PlayerComponent;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class HelpCommand implements Command {

    private final List<Command> orderedCommands;

    public HelpCommand(Collection<Command> registeredCommands) {
        orderedCommands = registeredCommands.stream()
            .sorted(Comparator.comparing(Command::getPhrase))
            .toList();
    }

    @Override
    public String getPhrase() {
        return "help";
    }

    @Override
    public CommandDescription getDescription() {
        return CommandDescription.of("List a description for every registered command");
    }

    @Override
    public void execute(PlayerComponent playerComponent, List<String> arguments) {
        var chat = playerComponent.getEntity().getComponentOrThrow(PlayerChatComponent.class);

        for (var command : orderedCommands) {
            var commandPhrase = "%s%s".formatted(CommandManager.COMMAND_PREFIX, command.getPhrase());

            if (command.getDescription().getArgumentDescriptions().isEmpty()) {
                chat.sendGameMessage("%s - %s.".formatted(commandPhrase, command.getDescription().getDescription()));
                continue;
            }

            var argumentParameters = command.getDescription().getArgumentDescriptions().stream()
                .map(argument -> "<%s : %s%s>".formatted(
                    argument.getArgumentName(),
                    argument.getArgumentType().getSimpleName(),
                    argument.isMandatory() ? "" : "?"
                ))
                .toList();

            chat.sendGameMessage("%s %s - %s.".formatted(
                commandPhrase,
                String.join(" ", argumentParameters),
                command.getDescription().getDescription()
            ));
        }
    }
}
