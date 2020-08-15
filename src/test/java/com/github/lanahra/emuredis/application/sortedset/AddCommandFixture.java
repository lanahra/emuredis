package com.github.lanahra.emuredis.application.sortedset;

public class AddCommandFixture {

    private AddCommandFixture() {
        // fixture class
    }

    public static AddCommand anAddCommand() {
        return new AddCommand("key", "value", 1.0);
    }
}