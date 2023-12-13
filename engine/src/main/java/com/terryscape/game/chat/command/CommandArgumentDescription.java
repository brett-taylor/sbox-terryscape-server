package com.terryscape.game.chat.command;

public class CommandArgumentDescription {

    public static CommandArgumentDescription mandatory(Class<?> argumentType, String argumentName) {
        return new CommandArgumentDescription(argumentType, argumentName, true);
    }

    public static CommandArgumentDescription optional(Class<?> argumentType, String argumentName) {
        return new CommandArgumentDescription(argumentType, argumentName, false);
    }

    private final Class<?> argumentType;

    private final String argumentName;

    private final boolean mandatory;

    private CommandArgumentDescription(Class<?> argumentType, String argumentName, boolean mandatory) {
        this.argumentType = argumentType;
        this.argumentName = argumentName;
        this.mandatory = mandatory;
    }

    public Class<?> getArgumentType() {
        return argumentType;
    }

    public String getArgumentName() {
        return argumentName;
    }

    public boolean isMandatory() {
        return mandatory;
    }
}
