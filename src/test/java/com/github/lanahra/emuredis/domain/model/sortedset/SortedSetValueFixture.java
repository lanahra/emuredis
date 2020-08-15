package com.github.lanahra.emuredis.domain.model.sortedset;

public class SortedSetValueFixture {

    private SortedSetValueFixture() {
        // fixture class
    }

    public static SortedSetValue aSortedSetValue() {
        SortedSetValue value = SortedSetValue.empty();
        value.add(Member.from("a", 1.0));
        value.add(Member.from("b", 2.0));
        value.add(Member.from("c", 3.0));
        value.add(Member.from("d", 4.0));
        return value;
    }
}