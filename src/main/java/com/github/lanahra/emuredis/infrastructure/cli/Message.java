package com.github.lanahra.emuredis.infrastructure.cli;

public class Message {

    static final String REPLY_OK = "OK";
    static final String REPLY_NIL = "(nil)";
    static final String REPLY_STRING = "\"%s\"";
    static final String REPLY_INTEGER = "(integer) %d";
    static final String REPLY_EMPTY_SET = "(empty list or set)";

    static final String ERROR_UNKNOWN_COMMAND = "(error) ERR Unknown or disabled command '%s'";
    static final String ERROR_WRONG_ARGUMENT_NUMBER =
            "(error) ERR wrong number of arguments for '%s' command";
    static final String ERROR_WRONG_TYPE =
            "(error) WRONGTYPE Operation against a key holding the wrong kind of value";
    static final String ERROR_SYNTAX = "(error) ERR syntax error";
    static final String ERROR_INTEGER_REPRESENTATION =
            "(error) ERR value is not an integer or out of range";
    static final String ERROR_FLOAT_REPRESENTATION = "(error) ERR value is not a valid float";

    private Message() {
        // utility class
    }
}
