package com.github.lanahra.emuredis.domain.model.sortedset;

import com.github.lanahra.emuredis.domain.model.Value;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class SortedSetValue extends Value {

    SortedSet<Member> members;

    private SortedSetValue() {
        super();
        this.members = new TreeSet<>();
    }

    public static SortedSetValue empty() {
        return new SortedSetValue();
    }

    public int add(Member member) {
        if (contains(member)) {
            members.removeIf(member::equals);
            members.add(member);
            return 0;
        } else {
            members.add(member);
            return 1;
        }
    }

    private boolean contains(Member member) {
        return new ArrayList<>(this.members).contains(member);
    }

    public int cardinality() {
        return members.size();
    }

    public int rank(Member member) {
        List<Member> members = new ArrayList<>(this.members);
        int rank = members.indexOf(member);
        if (rank == -1) {
            throw new MemberNotFoundException(member);
        }
        return rank;
    }

    public List<Member> range(int start, int stop) {
        List<Member> members = new ArrayList<>(this.members);
        int size = members.size();
        int fromIndex = fromIndex(start, size);
        int toIndex = toIndex(stop, size);
        return isInvalidRange(fromIndex, toIndex)
                ? Collections.emptyList()
                : members.subList(fromIndex, toIndex + 1);
    }

    private int fromIndex(int start, int size) {
        return start < 0 ? Math.max(size + start, 0) : start;
    }

    private int toIndex(int stop, int size) {
        return stop < 0 ? size + stop : Math.min(stop, size - 1);
    }

    private boolean isInvalidRange(int fromIndex, int toIndex) {
        int size = members.size();
        return fromIndex > toIndex || fromIndex > size - 1 || toIndex < 0;
    }

    @Override
    public boolean equals(Object other) {
        return other != null
                && getClass().equals(other.getClass())
                && equalsCasted((SortedSetValue) other);
    }

    private boolean equalsCasted(SortedSetValue other) {
        return members.equals(other.members);
    }

    @Override
    public int hashCode() {
        return members.hashCode();
    }
}
