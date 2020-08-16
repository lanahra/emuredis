package com.github.lanahra.emuredis.infrastructure.cli;

import static com.github.lanahra.emuredis.infrastructure.cli.Message.ERROR_INTEGER_REPRESENTATION;
import static com.github.lanahra.emuredis.infrastructure.cli.Message.ERROR_SYNTAX;
import static com.github.lanahra.emuredis.infrastructure.cli.Message.ERROR_UNKNOWN_COMMAND;
import static com.github.lanahra.emuredis.infrastructure.cli.Message.ERROR_WRONG_ARGUMENT_NUMBER;
import static com.github.lanahra.emuredis.infrastructure.cli.Message.ERROR_WRONG_TYPE;
import static com.github.lanahra.emuredis.infrastructure.cli.Message.REPLY_INTEGER;
import static com.github.lanahra.emuredis.infrastructure.cli.Message.REPLY_NIL;
import static com.github.lanahra.emuredis.infrastructure.cli.Message.REPLY_OK;
import static com.github.lanahra.emuredis.infrastructure.cli.Message.REPLY_STRING;

import com.github.lanahra.emuredis.application.string.SetCommand;
import com.github.lanahra.emuredis.application.string.StringApplicationService;
import com.github.lanahra.emuredis.domain.model.KeyNotFoundException;
import com.github.lanahra.emuredis.domain.model.ValueWrongTypeException;
import com.github.lanahra.emuredis.domain.model.string.IntegerRepresentationException;
import com.github.lanahra.emuredis.domain.model.string.StringValue;

public class StringCommandParser implements Parser {

    private final StringApplicationService service;

    public StringCommandParser(StringApplicationService service) {
        this.service = service;
    }

    @Override
    public String parse(String[] args) {
        assert args.length > 0;

        Command command = Command.valueOf(args[0].toUpperCase());

        switch (command) {
            case SET:
                return parseSetCommand(args);
            case GET:
                return parseGetCommand(args);
            case INCR:
                return parseIncrCommand(args);
            default:
                return String.format(ERROR_UNKNOWN_COMMAND, command);
        }
    }

    private String parseSetCommand(String[] args) {
        if (args.length < 3) {
            return String.format(ERROR_WRONG_ARGUMENT_NUMBER, args[0]);
        }
        try {
            SetCommand command = setCommandFrom(args);
            service.setValue(command);
            return REPLY_OK;
        } catch (NumberFormatException e) {
            return ERROR_INTEGER_REPRESENTATION;
        } catch (CommandSyntaxException e) {
            return ERROR_SYNTAX;
        }
    }

    private SetCommand setCommandFrom(String[] args) {
        if (args.length > 3) {
            if (args.length != 5) {
                throw new CommandSyntaxException();
            }
            return new SetCommand(args[1], args[2], Long.valueOf(args[4]));
        }
        return new SetCommand(args[1], args[2]);
    }

    private String parseGetCommand(String[] args) {
        if (args.length != 2) {
            return String.format(ERROR_WRONG_ARGUMENT_NUMBER, args[0]);
        }
        try {
            StringValue value = service.getValueOf(args[1]);
            return String.format(REPLY_STRING, value.value());
        } catch (KeyNotFoundException e) {
            return REPLY_NIL;
        } catch (ValueWrongTypeException e) {
            return ERROR_WRONG_TYPE;
        }
    }

    private String parseIncrCommand(String[] args) {
        if (args.length != 2) {
            return String.format(ERROR_WRONG_ARGUMENT_NUMBER, args[0]);
        }
        try {
            long result = service.incrementValueOf(args[1]);
            return String.format(REPLY_INTEGER, result);
        } catch (ValueWrongTypeException e) {
            return ERROR_WRONG_TYPE;
        } catch (IntegerRepresentationException e) {
            return ERROR_INTEGER_REPRESENTATION;
        }
    }
}
