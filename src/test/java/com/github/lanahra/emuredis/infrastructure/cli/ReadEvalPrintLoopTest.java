package com.github.lanahra.emuredis.infrastructure.cli;

import static com.github.lanahra.emuredis.infrastructure.cli.Message.ERROR_INTEGER_REPRESENTATION;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.Before;
import org.junit.Test;

public class ReadEvalPrintLoopTest {

    @Test
    public void readsCommandFromInputAndPrintsEvalToOutput() {
        when(configuration.in()).thenReturn(input("set a b\nquit\n"));

        when(parser.parse(new String[]{"set", "a", "b"})).thenReturn("OK");

        repl().start();

        assertThat(out.toString(), is("OK\nOK\n"));
    }

    @Test
    public void readsCommandFromInputAndPrintsEvalToErr() {
        when(configuration.in()).thenReturn(input("incr a\nquit\n"));

        when(parser.parse(new String[]{"incr", "a"})).thenReturn(ERROR_INTEGER_REPRESENTATION);

        repl().start();

        assertThat(err.toString(), is("(error) ERR value is not an integer or out of range\n"));
        assertThat(out.toString(), is("OK\n"));
    }

    @Test
    public void readsCommandWithQuotedWordsFromInputAndPrintsEvalToOutput() {
        when(configuration.in()).thenReturn(input("set \"a key\" \"a value\"\nquit\n"));

        when(parser.parse(new String[]{"set", "a key", "a value"})).thenReturn("OK");

        repl().start();

        assertThat(out.toString(), is("OK\nOK\n"));
    }

    @Test
    public void readsEmptyCommandFromInput() {
        when(configuration.in()).thenReturn(input("\nquit\n"));

        when(parser.parse(new String[]{})).thenReturn("");

        repl().start();

        assertThat(out.toString(), is("\nOK\n"));
    }

    private InputStream input(String input) {
        return new ByteArrayInputStream(input.getBytes());
    }

    private ReadEvalPrintLoop repl() {
        return new ReadEvalPrintLoop(configuration, parser);
    }

    @Before
    public void setup() {
        when(configuration.out()).thenReturn(new PrintStream(out));
        when(configuration.err()).thenReturn(new PrintStream(err));
    }

    private InputStream in;
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();
    private final InputOutputConfiguration configuration = mock(InputOutputConfiguration.class);
    private final Parser parser = mock(Parser.class);
}