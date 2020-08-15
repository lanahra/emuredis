package com.github.lanahra.emuredis.domain.model;

public class KeyFixture {

    private KeyFixture() {
        // fixture class
    }

    public static Key aKey() {
        return Key.from("key");
    }
}