package com.github.lanahra.emuredis.application.sortedset;

import com.github.lanahra.emuredis.domain.model.Key;
import com.github.lanahra.emuredis.domain.model.sortedset.Member;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class IsAnAddCommand extends TypeSafeMatcher<AddCommand> {

    private final String key;
    private final String value;
    private final double score;

    private IsAnAddCommand(String key, String value, double score) {
        this.key = key;
        this.value = value;
        this.score = score;
    }

    public static IsAnAddCommand anAddCommandWith(String key, String value, double score) {
        return new IsAnAddCommand(key, value, score);
    }

    @Override
    protected boolean matchesSafely(AddCommand item) {
        Member member = Member.from(value, score);
        return item.key().equals(Key.from(key))
                && item.member().equals(member)
                && item.member().compareTo(member) == 0;
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("an add command with key ")
                .appendValue(key)
                .appendText(" and value ")
                .appendValue(value)
                .appendText(" and score ")
                .appendValue(score);
    }
}
