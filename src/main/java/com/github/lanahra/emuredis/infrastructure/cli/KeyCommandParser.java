package com.github.lanahra.emuredis.infrastructure.cli;

import static com.github.lanahra.emuredis.infrastructure.cli.Message.ERROR_UNKNOWN_COMMAND;
import static com.github.lanahra.emuredis.infrastructure.cli.Message.ERROR_WRONG_ARGUMENT_NUMBER;
import static com.github.lanahra.emuredis.infrastructure.cli.Message.REPLY_INTEGER;

import com.github.lanahra.emuredis.application.KeyApplicationService;
import java.util.Arrays;

public class KeyCommandParser implements Parser {

    private final KeyApplicationService service;

    public KeyCommandParser(KeyApplicationService service) {
        this.service = service;
    }

    @Override
    public String parse(String[] args) {
        assert args.length > 0;

        Command command = Command.valueOf(args[0].toUpperCase());

        if (command.equals(Command.DBSIZE)) {
            return parseDbSizeCommand(args);
        } else if (command.equals(Command.DEL)) {
            return parseDelCommand(args);
        } else {
            return String.format(ERROR_UNKNOWN_COMMAND, command);
        }
    }

    private String parseDbSizeCommand(String[] args) {
        if (args.length > 1) {
            return String.format(ERROR_WRONG_ARGUMENT_NUMBER, args[0]);
        }
        int size = service.dbSize();
        return String.format(REPLY_INTEGER, size);
    }

    private String parseDelCommand(String[] args) {
        if (args.length == 1) {
            return String.format(ERROR_WRONG_ARGUMENT_NUMBER, args[0]);
        }
        int deleted = service.deleteKeys(Arrays.copyOfRange(args, 1, args.length));
        return String.format(REPLY_INTEGER, deleted);
    }
}
