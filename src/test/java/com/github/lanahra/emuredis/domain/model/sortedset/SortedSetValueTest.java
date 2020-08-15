package com.github.lanahra.emuredis.domain.model.sortedset;

import static com.github.lanahra.emuredis.domain.model.sortedset.SortedSetValueFixture.aSortedSetValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

import com.google.common.testing.EqualsTester;
import java.util.List;
import org.junit.Test;

public class SortedSetValueTest {

    @Test
    public void fulfillsEqualsAndHashCodeContract() {
        new EqualsTester()
                .addEqualityGroup(SortedSetValue.empty(), SortedSetValue.empty())
                .addEqualityGroup(aSortedSetValue(), aSortedSetValue())
                .testEquals();
    }

    @Test
    public void addsMemberToEmptySortedSet() {
        SortedSetValue value = SortedSetValue.empty();

        int added = value.add(Member.from("a", 1.0));

        assertThat(added, is(1));
    }

    @Test
    public void doesNotAddDuplicateMember() {
        SortedSetValue value = SortedSetValue.empty();

        int firstAdded = value.add(Member.from("a", 1.0));
        int secondAdded = value.add(Member.from("a", 2.0));

        assertThat(firstAdded, is(1));
        assertThat(secondAdded, is(0));
    }

    @Test
    public void retrievesSortedSetCardinality() {
        SortedSetValue value = SortedSetValue.empty();

        value.add(Member.from("a", 1.0));
        value.add(Member.from("a", 2.0));
        value.add(Member.from("b", 3.0));

        assertThat(value.cardinality(), is(2));
    }

    @Test
    public void retrievesMemberRankInSortedSet() {
        SortedSetValue value = SortedSetValue.empty();
        Member a = Member.from("a", 3.0);
        Member b = Member.from("b", 2.0);
        Member c = Member.from("c", 1.0);

        value.add(a);
        value.add(b);
        value.add(c);

        assertThat(value.rank(a), is(2));
        assertThat(value.rank(b), is(1));
        assertThat(value.rank(c), is(0));
    }

    @Test(expected = MemberNotFoundException.class)
    public void throwsExceptionForRetrievingRankFromNonMemberOfSortedSet() {
        SortedSetValue value = SortedSetValue.empty();

        value.rank(Member.from("a"));
    }

    @Test
    public void reinsertsDuplicateMemberInSortedSetWithUpdatedScore() {
        SortedSetValue value = SortedSetValue.empty();

        Member a = Member.from("a", 1.0);
        Member b = Member.from("b", 2.0);
        Member c = Member.from("a", 3.0);

        value.add(a);
        value.add(b);
        value.add(c);

        assertThat(value.rank(a), is(1));
        assertThat(value.rank(b), is(0));
        assertThat(value.rank(c), is(1));
    }

    @Test
    public void retrievesEmptyRangeFromSortedSetForStartGreaterThanStop() {
        SortedSetValue value = aSortedSetValue();

        assertThat(value.range(1, 0), is(empty()));
        assertThat(value.range(-1, -2), is(empty()));
    }

    @Test
    public void retrievesEmptyRangeFromSortedSetForStartGreaterThanLargestIndexInTheSortedSet() {
        SortedSetValue value = aSortedSetValue();

        assertThat(value.range(4, 4), is(empty()));
    }

    @Test
    public void retrievesEmptyRangeFromSortedSetForStopLessThanLowestIndexInTheSortedSet() {
        SortedSetValue value = aSortedSetValue();

        assertThat(value.range(-5, -5), is(empty()));
    }

    @Test
    public void retrievesRangeFromSortedSetForStopGreaterThanLargestIndexInTheSortedSet() {
        SortedSetValue value = aSortedSetValue();

        List<Member> range = value.range(1, 10);

        assertThat(range, contains(Member.from("b"), Member.from("c"), Member.from("d")));
    }

    @Test
    public void retrievesRangeFromSortedSetForStartLessThanLowestIndexInTheSortedSet() {
        SortedSetValue value = aSortedSetValue();

        List<Member> range = value.range(-10, -3);

        assertThat(range, contains(Member.from("a"), Member.from("b")));
    }

    @Test
    public void retrievesRangeWithOneElementFromSortedSet() {
        SortedSetValue value = aSortedSetValue();

        List<Member> range = value.range(1, 1);

        assertThat(range, contains(Member.from("b")));
    }

    @Test
    public void retrievesRangeFromSortedSetWithNegativeIndexes() {
        SortedSetValue value = aSortedSetValue();

        List<Member> range = value.range(-2, -1);

        assertThat(range, contains(Member.from("c"), Member.from("d")));
    }
}
