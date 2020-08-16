package com.github.lanahra.emuredis.infrastructure.cli;

import static com.github.lanahra.emuredis.application.sortedset.IsARangeCommand.aRangeCommandWith;
import static com.github.lanahra.emuredis.application.sortedset.IsAnAddCommand.anAddCommandWith;
import static com.github.lanahra.emuredis.domain.model.sortedset.MemberFixture.aRangeOfMembers;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

import com.github.lanahra.emuredis.application.sortedset.SortedSetApplicationService;
import com.github.lanahra.emuredis.domain.model.KeyNotFoundException;
import com.github.lanahra.emuredis.domain.model.ValueWrongTypeException;
import com.github.lanahra.emuredis.domain.model.sortedset.MemberNotFoundException;
import java.util.Collections;
import org.junit.Test;

public class SortedSetCommandParserTest {

    @Test
    public void parsingUnknownCommandReturnsError() {
        String[] args = {"SET"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR Unknown or disabled command 'SET'"));
    }

    @Test
    public void parsesZAddCommand() {
        String[] args = {"zadd", "a", "1", "b"};

        when(service.addMember(argThat(is(anAddCommandWith("a", "b", 1.0))))).thenReturn(1);

        String output = parser.parse(args);

        assertThat(output, is("(integer) 1"));
    }

    @Test
    public void parsesZAddCommandWithPositiveInfinity() {
        String[] args = {"zadd", "a", "+inf", "b"};

        when(service.addMember(argThat(is(anAddCommandWith("a", "b", Double.POSITIVE_INFINITY)))))
                .thenReturn(1);

        String output = parser.parse(args);

        assertThat(output, is("(integer) 1"));
    }

    @Test
    public void parsesZAddCommandWithPositiveInfinityWithoutPlusSign() {
        String[] args = {"zadd", "a", "inf", "b"};

        when(service.addMember(argThat(is(anAddCommandWith("a", "b", Double.POSITIVE_INFINITY)))))
                .thenReturn(1);

        String output = parser.parse(args);

        assertThat(output, is("(integer) 1"));
    }

    @Test
    public void parsesZAddCommandWithNegativeInfinity() {
        String[] args = {"zadd", "a", "-inf", "b"};

        when(service.addMember(argThat(is(anAddCommandWith("a", "b", Double.NEGATIVE_INFINITY)))))
                .thenReturn(1);

        String output = parser.parse(args);

        assertThat(output, is("(integer) 1"));
    }

    @Test
    public void parsingZAddCommandWithNonFloatScoreReturnsError() {
        String[] args = {"zadd", "a", "non float", "b"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR value is not a valid float"));
    }

    @Test
    public void parsingZAddCommandWithWrongNumberOfArgumentsReturnsError() {
        String[] args = {"zadd", "a"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR wrong number of arguments for 'zadd' command"));
    }

    @Test
    public void parsingZAddCommandWithKeyOfWrongTypeReturnsError() {
        String[] args = {"zadd", "a", "1", "b"};

        when(service.addMember(argThat(is(anAddCommandWith("a", "b", 1.0)))))
                .thenThrow(ValueWrongTypeException.class);

        String output = parser.parse(args);

        assertThat(
                output,
                is("(error) WRONGTYPE Operation against a key holding the wrong kind of value"));
    }

    @Test
    public void parsesZCardCommand() {
        String[] args = {"zcard", "a"};

        when(service.cardinalityOfSortedSetOf("a")).thenReturn(2);

        String output = parser.parse(args);

        assertThat(output, is("(integer) 2"));
    }

    @Test
    public void parsingZCardCommandWithWrongNumberOfArgumentsReturnsError() {
        String[] args = {"zcard", "a", "a"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR wrong number of arguments for 'zcard' command"));
    }

    @Test
    public void parsingZCardCommandWithKeyOfWrongTypeReturnsError() {
        String[] args = {"zcard", "a"};

        when(service.cardinalityOfSortedSetOf("a")).thenThrow(ValueWrongTypeException.class);

        String output = parser.parse(args);

        assertThat(
                output,
                is("(error) WRONGTYPE Operation against a key holding the wrong kind of value"));
    }

    @Test
    public void parsesZRankCommand() {
        String[] args = {"zrank", "a", "b"};

        when(service.rankInSortedSet("a", "b")).thenReturn(2);

        String output = parser.parse(args);

        assertThat(output, is("(integer) 2"));
    }

    @Test
    public void parsingZRankCommandOnNonExistentKeyReturnsNil() {
        String[] args = {"zrank", "a", "b"};

        when(service.rankInSortedSet("a", "b")).thenThrow(KeyNotFoundException.class);

        String output = parser.parse(args);

        assertThat(output, is("(nil)"));
    }

    @Test
    public void parsingZRankCommandOnNonExistentMemberInSortedSetReturnsNil() {
        String[] args = {"zrank", "a", "b"};

        when(service.rankInSortedSet("a", "b")).thenThrow(MemberNotFoundException.class);

        String output = parser.parse(args);

        assertThat(output, is("(nil)"));
    }

    @Test
    public void parsingZRankCommandWithWrongNumberOfArgumentsReturnsError() {
        String[] args = {"zrank", "a"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR wrong number of arguments for 'zrank' command"));
    }

    @Test
    public void parsingZRankCommandWithKeyOfWrongTypeReturnsError() {
        String[] args = {"zrank", "a", "b"};

        when(service.rankInSortedSet("a", "b")).thenThrow(ValueWrongTypeException.class);

        String output = parser.parse(args);

        assertThat(
                output,
                is("(error) WRONGTYPE Operation against a key holding the wrong kind of value"));
    }

    @Test
    public void parsesZRangeCommand() {
        String[] args = {"zrange", "a", "0", "-1"};

        when(service.rangeFromSortedSet(argThat(is(aRangeCommandWith("a", 0, -1)))))
                .thenReturn(aRangeOfMembers());

        String output = parser.parse(args);

        assertThat(output, is("1) \"a\"\n2) \"b\"\n3) \"c\"\n4) \"d\""));
    }

    @Test
    public void parsingZRangeCommandWithEmptySortedSetReturnsEmptyList() {
        String[] args = {"zrange", "a", "0", "-1"};

        when(service.rangeFromSortedSet(argThat(is(aRangeCommandWith("a", 0, -1)))))
                .thenReturn(Collections.emptyList());

        String output = parser.parse(args);

        assertThat(output, is("(empty list or set)"));
    }

    @Test
    public void parsingZRangeCommandWithNonExistentKeyReturnsEmptySet() {
        String[] args = {"zrange", "a", "0", "-1"};

        when(service.rangeFromSortedSet(argThat(is(aRangeCommandWith("a", 0, -1)))))
                .thenThrow(KeyNotFoundException.class);

        String output = parser.parse(args);

        assertThat(output, is("(empty list or set)"));
    }

    @Test
    public void parsingZRangeCommandWithWrongNumberOfArgumentsReturnsError() {
        String[] args = {"zrange", "a"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR wrong number of arguments for 'zrange' command"));
    }

    @Test
    public void parsingZRangeCommandWithNonIntegerStartReturnsError() {
        String[] args = {"zrange", "a", "non integer", "-1"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR value is not an integer or out of range"));
    }

    @Test
    public void parsingZRangeCommandWithNonIntegerStopReturnsError() {
        String[] args = {"zrange", "a", "0", "non integer"};

        String output = parser.parse(args);

        assertThat(output, is("(error) ERR value is not an integer or out of range"));
    }

    @Test
    public void parsingZRangeCommandWithKeyOfWrongTypeReturnsError() {
        String[] args = {"zrange", "a", "0", "-1"};

        when(service.rangeFromSortedSet(argThat(is(aRangeCommandWith("a", 0, -1)))))
                .thenThrow(ValueWrongTypeException.class);

        String output = parser.parse(args);

        assertThat(
                output,
                is("(error) WRONGTYPE Operation against a key holding the wrong kind of value"));
    }

    private final SortedSetApplicationService service = mock(SortedSetApplicationService.class);
    private final SortedSetCommandParser parser = new SortedSetCommandParser(service);
}
