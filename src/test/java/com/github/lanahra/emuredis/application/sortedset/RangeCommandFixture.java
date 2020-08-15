package com.github.lanahra.emuredis.application.sortedset;

public class RangeCommandFixture {

    private RangeCommandFixture() {
        // fixture class
    }

    public static RangeCommand aRangeCommand() {
        return new RangeCommand("key", 1, 2);
    }
}