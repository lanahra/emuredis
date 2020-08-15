package com.github.lanahra.emuredis.domain.model.sortedset;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.greaterThan;

import com.google.common.testing.EqualsTester;
import org.junit.Test;

public class MemberTest {

    @Test
    public void fulfillsEqualsAndHashCodeContract() {
        new EqualsTester()
                .addEqualityGroup(Member.from("", 1.0), Member.from("", 2.0))
                .addEqualityGroup(Member.from("value", 1.0), Member.from("value", 1.0))
                .addEqualityGroup(Member.from("VALUE", 1.0), Member.from("VALUE", 2.0))
                .testEquals();
    }

    @Test
    public void membersWithSameScoreHaveSameOrdering() {
        assertThat(Member.from("value", 1.0).compareTo(Member.from("value", 1.0)), is(0));
    }

    @Test
    public void orderingRelationIsAssociative() {
        Member a = Member.from("a", 1.0);
        Member b = Member.from("b", 2.0);
        assertThat(a.compareTo(b), is(-b.compareTo(a)));
    }

    @Test
    public void orderingRelationIsTransitive() {
        Member a = Member.from("a", 3.0);
        Member b = Member.from("b", 2.0);
        Member c = Member.from("c", 1.0);
        assertThat(a.compareTo(b), is(greaterThan(0)));
        assertThat(b.compareTo(c), is(greaterThan(0)));
        assertThat(a.compareTo(c), is(greaterThan(0)));
    }
}
