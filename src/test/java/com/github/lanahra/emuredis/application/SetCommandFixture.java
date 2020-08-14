package com.github.lanahra.emuredis.application;

public class SetCommandFixture {

    private SetCommandFixture() {
        // fixture class
    }

    public static SetCommand aSetCommand() {
        return new SetCommand("key", "value", null);
    }

    public static SetCommand aSetCommandWithExpiration() {
        return new SetCommand("key", "value", 10L);
    }
}
