package com.github.lanahra.emuredis.infrastructure.cli;

public class InvalidCommandException extends RuntimeException {

    public InvalidCommandException(Command command) {
        super(String.format("Invalid command '%s'", command.toString()));
    }
}
