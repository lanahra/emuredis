package com.github.lanahra.emuredis.infrastructure.cli;

public class CommandSyntaxException extends RuntimeException {

    public CommandSyntaxException() {
        super("Syntax error");
    }
}
