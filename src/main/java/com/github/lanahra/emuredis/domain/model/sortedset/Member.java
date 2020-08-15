package com.github.lanahra.emuredis.domain.model.sortedset;

public class Member implements Comparable<Member> {

    private final String value;
    private final double score;

    private Member(String value, double score) {
        this.value = value;
        this.score = score;
    }

    public static Member from(String value, double score) {
        return new Member(value, score);
    }

    public static Member from(String value) {
        return new Member(value, 0.0);
    }

    @Override
    public int compareTo(Member other) {
        return Double.compare(score, other.score);
    }

    @Override
    public boolean equals(Object other) {
        return other != null
                && getClass().equals(other.getClass())
                && equalsCasted((Member) other);
    }

    private boolean equalsCasted(Member other) {
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
