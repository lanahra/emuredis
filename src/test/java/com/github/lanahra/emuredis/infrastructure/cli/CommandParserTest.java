package com.github.lanahra.emuredis.infrastructure.cli;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class CommandParserTest {

    @Test
    public void parsingNoArgsReturnsEmptyString() {
        String[] args = {};

        String output = parser.parse(args);

        assertThat(output, is(""));
    }

    @Test
    public void parsingUnknownCommandReturnsError() {
        String[] args = {"unknown"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR Unknown or disabled command 'unknown'"));
    }

    @Test
    public void delegatesKeyCommandsToKeyCommandParser() {
        when(keyCommandParser.parse(any(String[].class))).thenReturn("OK");

        assertThat(parser.parse(new String[] {"dbsize"}), is("OK"));
        assertThat(parser.parse(new String[] {"del"}), is("OK"));
    }

    @Test
    public void delegatesStringCommandsToStringCommandParser() {
        when(stringCommandParser.parse(any(String[].class))).thenReturn("OK");

        assertThat(parser.parse(new String[] {"set"}), is("OK"));
        assertThat(parser.parse(new String[] {"get"}), is("OK"));
        assertThat(parser.parse(new String[] {"incr"}), is("OK"));
    }

    @Test
    public void delegatesSortedSetCommandsToSortedSetCommandParser() {
        when(sortedSetCommandParser.parse(any(String[].class))).thenReturn("OK");

        assertThat(parser.parse(new String[] {"zadd"}), is("OK"));
        assertThat(parser.parse(new String[] {"zcard"}), is("OK"));
        assertThat(parser.parse(new String[] {"zrank"}), is("OK"));
        assertThat(parser.parse(new String[] {"zrange"}), is("OK"));
    }

    private final Parser keyCommandParser = mock(Parser.class);
    private final Parser stringCommandParser = mock(Parser.class);
    private final Parser sortedSetCommandParser = mock(Parser.class);
    private final Parser parser =
            new CommandParser(keyCommandParser, stringCommandParser, sortedSetCommandParser);
}
