package com.github.lanahra.emuredis.infrastructure.cli;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.lanahra.emuredis.application.KeyApplicationService;
import org.junit.Test;

public class KeyCommandParserTest {

    @Test
    public void parsingUnknownCommandReturnsError() {
        String[] args = {"SET"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR Unknown or disabled command 'SET'"));
    }

    @Test
    public void parsesDbSizeCommand() {
        String[] args = {"DBSIZE"};

        when(service.dbSize()).thenReturn(2);

        String output = parser.parse(args);

        assertThat(output, is("(integer) 2"));
    }

    @Test
    public void parsingDbSizeCommandWithWrongNumberOfArgumentsReturnsError() {
        String[] args = {"DBSIZE", "a", "b", "c"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR wrong number of arguments for 'DBSIZE' command"));
    }

    @Test
    public void parsesDelCommand() {
        String[] args = {"DEL", "a", "b", "c"};

        when(service.deleteKeys("a", "b", "c")).thenReturn(2);

        String output = parser.parse(args);

        assertThat(output, is("(integer) 2"));
    }

    @Test
    public void parsingDelCommandWithWrongNumberOfArgumentsReturnsError() {
        String[] args = {"DEL"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR wrong number of arguments for 'DEL' command"));
    }

    private final KeyApplicationService service = mock(KeyApplicationService.class);
    private final KeyCommandParser parser = new KeyCommandParser(service);
}