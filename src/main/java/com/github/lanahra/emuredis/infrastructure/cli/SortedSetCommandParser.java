package com.github.lanahra.emuredis.infrastructure.cli;

import static com.github.lanahra.emuredis.infrastructure.cli.Message.ERROR_FLOAT_REPRESENTATION;
import static com.github.lanahra.emuredis.infrastructure.cli.Message.ERROR_INTEGER_REPRESENTATION;
import static com.github.lanahra.emuredis.infrastructure.cli.Message.ERROR_UNKNOWN_COMMAND;
import static com.github.lanahra.emuredis.infrastructure.cli.Message.ERROR_WRONG_ARGUMENT_NUMBER;
import static com.github.lanahra.emuredis.infrastructure.cli.Message.ERROR_WRONG_TYPE;
import static com.github.lanahra.emuredis.infrastructure.cli.Message.REPLY_EMPTY_SET;
import static com.github.lanahra.emuredis.infrastructure.cli.Message.REPLY_INTEGER;
import static com.github.lanahra.emuredis.infrastructure.cli.Message.REPLY_NIL;

import com.github.lanahra.emuredis.application.sortedset.AddCommand;
import com.github.lanahra.emuredis.application.sortedset.RangeCommand;
import com.github.lanahra.emuredis.application.sortedset.SortedSetApplicationService;
import com.github.lanahra.emuredis.domain.model.KeyNotFoundException;
import com.github.lanahra.emuredis.domain.model.ValueWrongTypeException;
import com.github.lanahra.emuredis.domain.model.sortedset.Member;
import com.github.lanahra.emuredis.domain.model.sortedset.MemberNotFoundException;
import java.util.List;

public class SortedSetCommandParser implements Parser {

    private static final String POSITIVE_INFINITY = "inf";
    private static final String POSITIVE_INFINITY_WITH_SIGN = "+inf";
    private static final String NEGATIVE_INFINITY = "-inf";
    private final SortedSetApplicationService service;

    public SortedSetCommandParser(SortedSetApplicationService service) {
        this.service = service;
    }

    @Override
    public String parse(String[] args) {
        assert args.length > 0;

        Command command = Command.valueOf(args[0].toUpperCase());

        switch (command) {
            case ZADD:
                return parseZAddCommand(args);
            case ZCARD:
                return parseZCardCommand(args);
            case ZRANK:
                return parseZRankCommand(args);
            case ZRANGE:
                return parseZRangeCommand(args);
            default:
                return String.format(ERROR_UNKNOWN_COMMAND, command);
        }
    }

    private String parseZAddCommand(String[] args) {
        if (args.length != 4) {
            return String.format(ERROR_WRONG_ARGUMENT_NUMBER, args[0]);
        }
        try {
            AddCommand command = new AddCommand(args[1], args[3], scoreFrom(args[2]));
            int added = service.addMember(command);
            return String.format(REPLY_INTEGER, added);
        } catch (NumberFormatException e) {
            return ERROR_FLOAT_REPRESENTATION;
        } catch (ValueWrongTypeException e) {
            return ERROR_WRONG_TYPE;
        }
    }

    private double scoreFrom(String arg) {
        if (arg.equalsIgnoreCase(POSITIVE_INFINITY)
                || arg.equalsIgnoreCase(POSITIVE_INFINITY_WITH_SIGN)) {
            return Double.POSITIVE_INFINITY;
        } else if (arg.equalsIgnoreCase(NEGATIVE_INFINITY)) {
            return Double.NEGATIVE_INFINITY;
        } else {
            return Double.parseDouble(arg);
        }
    }

    private String parseZCardCommand(String[] args) {
        if (args.length != 2) {
            return String.format(ERROR_WRONG_ARGUMENT_NUMBER, args[0]);
        }
        try {
            int cardinality = service.cardinalityOfSortedSetOf(args[1]);
            return String.format(REPLY_INTEGER, cardinality);
        } catch (ValueWrongTypeException e) {
            return ERROR_WRONG_TYPE;
        }
    }

    private String parseZRankCommand(String[] args) {
        if (args.length != 3) {
            return String.format(ERROR_WRONG_ARGUMENT_NUMBER, args[0]);
        }
        try {
            int rank = service.rankInSortedSet(args[1], args[2]);
            return String.format(REPLY_INTEGER, rank);
        } catch (KeyNotFoundException | MemberNotFoundException e) {
            return REPLY_NIL;
        } catch (ValueWrongTypeException e) {
            return ERROR_WRONG_TYPE;
        }
    }

    private String parseZRangeCommand(String[] args) {
        if (args.length != 4) {
            return String.format(ERROR_WRONG_ARGUMENT_NUMBER, args[0]);
        }
        try {
            RangeCommand command = rangeCommandFrom(args);
            List<Member> members = service.rangeFromSortedSet(command);
            return members.isEmpty() ? REPLY_EMPTY_SET : formattedOutputFor(members);
        } catch (KeyNotFoundException e) {
            return REPLY_EMPTY_SET;
        } catch (NumberFormatException e) {
            return ERROR_INTEGER_REPRESENTATION;
        } catch (ValueWrongTypeException e) {
            return ERROR_WRONG_TYPE;
        }
    }

    private RangeCommand rangeCommandFrom(String[] args) {
        return new RangeCommand(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
    }

    private String formattedOutputFor(List<Member> members) {
        StringBuilder builder = new StringBuilder();
        int i = 1;
        for (Member member : members) {
            builder.append(i).append(") \"").append(member.value()).append("\"\n");
            i++;
        }
        return builder.toString().stripTrailing();
    }
}
