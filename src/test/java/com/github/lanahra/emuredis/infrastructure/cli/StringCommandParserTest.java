package com.github.lanahra.emuredis.infrastructure.cli;

import static com.github.lanahra.emuredis.application.string.IsASetCommand.aSetCommandWith;
import static com.github.lanahra.emuredis.domain.model.string.StringValueFixture.aStringValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

import com.github.lanahra.emuredis.application.string.StringApplicationService;
import com.github.lanahra.emuredis.domain.model.KeyNotFoundException;
import com.github.lanahra.emuredis.domain.model.ValueWrongTypeException;
import com.github.lanahra.emuredis.domain.model.string.IntegerRepresentationException;
import org.junit.Test;

public class StringCommandParserTest {

    @Test
    public void parsingUnknownCommandReturnsError() {
        String[] args = {"DBSIZE"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR Unknown or disabled command 'DBSIZE'"));
    }

    @Test
    public void parsesSetCommand() {
        String[] args = {"set", "a", "b"};

        String output = parser.parse(args);

        verify(service).setValue(argThat(is(aSetCommandWith("a", "b"))));

        assertThat(output, is("OK"));
    }

    @Test
    public void parsesSetCommandWithExpiration() {
        String[] args = {"set", "a", "b", "ex", "10"};

        String output = parser.parse(args);

        verify(service).setValue(argThat(is(aSetCommandWith("a", "b", 10L))));

        assertThat(output, is("OK"));
    }

    @Test
    public void parsingsSetCommandWithNoKeysReturnsError() {
        String[] args = {"set"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR wrong number of arguments for 'set' command"));
    }

    @Test
    public void parsingSetCommandWithWrongNumberOfArgumentsReturnsError() {
        String[] args = {"set", "a"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR wrong number of arguments for 'set' command"));
    }

    @Test
    public void parsingSetCommandWithNonIntegerExpirationReturnsError() {
        String[] args = {"set", "a", "b", "ex", "non integer"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR value is not an integer or out of range"));
    }

    @Test
    public void parsingSetCommandWithIntegerOutOfRangeExpirationReturnsError() {
        String[] args = {"set", "a", "b", "ex", "99999999999999999999999999999999999"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR value is not an integer or out of range"));
    }

    @Test
    public void parsingSetCommandWithExpirationWithoutSecondsArgumentReturnsError() {
        String[] args = {"set", "a", "b", "ex"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR syntax error"));
    }

    @Test
    public void parsingSetCommandWithExpirationWithWrongNumberOfArgumentsReturnsError() {
        String[] args = {"set", "a", "b", "ex", "1", "2"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR syntax error"));
    }

    @Test
    public void parsesGetCommand() {
        String[] args = {"get", "a"};

        when(service.getValueOf("a")).thenReturn(aStringValue());

        String output = parser.parse(args);

        assertThat(output, is("\"value\""));
    }

    @Test
    public void parsingGetCommandWithNoKeysReturnsError() {
        String[] args = {"get"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR wrong number of arguments for 'get' command"));
    }

    @Test
    public void parsingGetCommandWithWrongNumberOfArgumentsReturnsError() {
        String[] args = {"get", "a", "b"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR wrong number of arguments for 'get' command"));
    }

    @Test
    public void parsingGetCommandOnNonExistentKeyReturnsNil() {
        String[] args = {"get", "a"};

        when(service.getValueOf("a")).thenThrow(KeyNotFoundException.class);

        String output = parser.parse(args);

        assertThat(output, is("(nil)"));
    }

    @Test
    public void parsingGetCommandOnKeyOfWrongTypeReturnsError() {
        String[] args = {"get", "a"};

        when(service.getValueOf("a")).thenThrow(ValueWrongTypeException.class);

        String output = parser.parse(args);

        assertThat(
                output,
                is("(error) WRONGTYPE Operation against a key holding the wrong kind of value"));
    }

    @Test
    public void parsesIncrCommand() {
        String[] args = {"incr", "a"};

        when(service.incrementValueOf("a")).thenReturn(2L);

        String output = parser.parse(args);

        assertThat(output, is("(integer) 2"));
    }

    @Test
    public void parsingIncrCommandOnKeyOfWrongTypeReturnsError() {
        String[] args = {"incr", "a"};

        when(service.incrementValueOf("a")).thenThrow(ValueWrongTypeException.class);

        String output = parser.parse(args);

        assertThat(
                output,
                is("(error) WRONGTYPE Operation against a key holding the wrong kind of value"));
    }

    @Test
    public void parsingIncrCommandOnKeyWithNonIntegerValueReturnsError() {
        String[] args = {"incr", "a"};

        when(service.incrementValueOf("a")).thenThrow(IntegerRepresentationException.class);

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR value is not an integer or out of range"));
    }

    @Test
    public void parsingIncrCommandWithNoKeysReturnsError() {
        String[] args = {"incr"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR wrong number of arguments for 'incr' command"));
    }

    @Test
    public void parsingIncrCommandWithWrongNumberOfArgumentsReturnsError() {
        String[] args = {"incr", "a", "b"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR wrong number of arguments for 'incr' command"));
    }

    private final StringApplicationService service = mock(StringApplicationService.class);
    private final StringCommandParser parser = new StringCommandParser(service);
}
