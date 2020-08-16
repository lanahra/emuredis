package com.github.lanahra.emuredis.infrastructure.cli;

import static com.github.lanahra.emuredis.infrastructure.cli.Message.ERROR_UNKNOWN_COMMAND;

public class CommandParser implements Parser {

    private final Parser keyCommandParser;
    private final Parser stringCommandParser;
    private final Parser sortedSetCommandParser;

    public CommandParser(
            Parser keyCommandParser, Parser stringCommandParser, Parser sortedSetCommandParser) {
        this.keyCommandParser = keyCommandParser;
        this.stringCommandParser = stringCommandParser;
        this.sortedSetCommandParser = sortedSetCommandParser;
    }

    public String parse(String[] args) {
        if (args.length == 0) {
            return "";
        }

        return parseCommand(args);
    }

    private String parseCommand(String[] args) {
        try {
            Command command = Command.valueOf(args[0].toUpperCase());
            Parser parser = parserFor(command);
            return parser.parse(args);
        } catch (IllegalArgumentException e) {
            return String.format(ERROR_UNKNOWN_COMMAND, args[0]);
        }
    }

    private Parser parserFor(Command command) {
        switch (command) {
            case DBSIZE:
            case DEL:
                return keyCommandParser;
            case SET:
            case GET:
            case INCR:
                return stringCommandParser;
            case ZADD:
            case ZCARD:
            case ZRANK:
            case ZRANGE:
                return sortedSetCommandParser;
            default:
                throw new InvalidCommandException(command);
        }
    }
}
