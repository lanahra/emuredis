package com.github.lanahra.emuredis.application.sortedset;

import com.github.lanahra.emuredis.domain.model.Key;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class IsARangeCommand extends TypeSafeMatcher<RangeCommand> {

    private final String key;
    private final int start;
    private final int stop;

    private IsARangeCommand(String key, int start, int stop) {
        this.key = key;
        this.start = start;
        this.stop = stop;
    }

    public static IsARangeCommand aRangeCommandWith(String key, int start, int stop) {
        return new IsARangeCommand(key, start, stop);
    }

    @Override
    protected boolean matchesSafely(RangeCommand item) {
        return item.key().equals(Key.from(key)) && item.start() == start && item.stop() == stop;
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("a range command with key ")
                .appendValue(key)
                .appendText(" and start ")
                .appendValue(start)
                .appendText(" and stop")
                .appendValue(stop);
    }
}
