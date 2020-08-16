package com.github.lanahra.emuredis.application.sortedset;

import static com.github.lanahra.emuredis.application.sortedset.AddCommandFixture.anAddCommand;
import static com.github.lanahra.emuredis.application.sortedset.RangeCommandFixture.aRangeCommand;
import static com.github.lanahra.emuredis.domain.model.KeyFixture.aKey;
import static com.github.lanahra.emuredis.domain.model.sortedset.SortedSetValueFixture.aSortedSetValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.lanahra.emuredis.application.SimpleTransaction;
import com.github.lanahra.emuredis.application.Transaction;
import com.github.lanahra.emuredis.domain.model.KeyNotFoundException;
import com.github.lanahra.emuredis.domain.model.KeyValueRepository;
import com.github.lanahra.emuredis.domain.model.sortedset.Member;
import com.github.lanahra.emuredis.domain.model.sortedset.SortedSetValue;
import java.util.List;
import org.junit.Test;

public class SortedSetApplicationServiceTest {

    @Test
    public void addsMemberToNewSortedSet() {
        AddCommand command = anAddCommand();

        when(repository.sortedSetFor(command.key())).thenThrow(KeyNotFoundException.class);

        int added = service.addMember(command);

        SortedSetValue expectedValue = SortedSetValue.empty();
        expectedValue.add(command.member());

        verify(repository).add(command.key(), expectedValue);
        assertThat(added, is(1));
    }

    @Test
    public void addsMemberToExistingSortedSet() {
        AddCommand command = anAddCommand();

        SortedSetValue value = aSortedSetValue();
        when(repository.sortedSetFor(command.key())).thenReturn(value);

        int added = service.addMember(command);

        SortedSetValue expectedValue = aSortedSetValue();
        expectedValue.add(command.member());

        assertThat(value, is(expectedValue));
        assertThat(added, is(1));
    }

    @Test
    public void updatesMemberScoreInExistingSortedSet() {
        AddCommand command = anAddCommand();

        SortedSetValue value = aSortedSetValue();
        value.add(Member.from("value", 5.0));
        when(repository.sortedSetFor(command.key())).thenReturn(value);

        int added = service.addMember(command);

        SortedSetValue expectedValue = aSortedSetValue();
        expectedValue.add(command.member());

        assertThat(value, is(expectedValue));
        assertThat(added, is(0));
    }

    @Test
    public void zeroCardinalityForNonExistentSortedSet() {
        when(repository.sortedSetFor(aKey())).thenThrow(KeyNotFoundException.class);

        int cardinality = service.cardinalityOfSortedSetOf("key");

        assertThat(cardinality, is(0));
    }

    @Test
    public void retrievesCardinalityOfExistentSortedSet() {
        when(repository.sortedSetFor(aKey())).thenReturn(aSortedSetValue());

        int cardinality = service.cardinalityOfSortedSetOf("key");

        assertThat(cardinality, is(4));
    }

    @Test
    public void retrievesRankFromMemberInExistentSortedSet() {
        when(repository.sortedSetFor(aKey())).thenReturn(aSortedSetValue());

        int rank = service.rankInSortedSet("key", "b");

        assertThat(rank, is(1));
    }

    @Test
    public void retrievesRangeFromSortedSet() {
        RangeCommand command = aRangeCommand();

        when(repository.sortedSetFor(aKey())).thenReturn(aSortedSetValue());

        List<Member> members = service.rangeFromSortedSet(command);

        assertThat(members, contains(Member.from("b"), Member.from("c")));
    }

    private final KeyValueRepository repository = mock(KeyValueRepository.class);
    private final Transaction transaction = new SimpleTransaction();
    private final SortedSetApplicationService service =
            new SortedSetApplicationService(repository, transaction);
}
