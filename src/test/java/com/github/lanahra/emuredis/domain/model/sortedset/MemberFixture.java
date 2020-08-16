package com.github.lanahra.emuredis.domain.model.sortedset;

import java.util.Arrays;
import java.util.List;

public class MemberFixture {

    private MemberFixture() {
        // fixture class
    }

    public static List<Member> aRangeOfMembers() {
        return Arrays.asList(Member.from("a"), Member.from("b"), Member.from("c"), Member.from("d"));
    }
}