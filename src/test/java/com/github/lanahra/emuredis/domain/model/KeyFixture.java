package com.github.lanahra.emuredis.domain.model;

import java.time.Instant;

public class KeyFixture {

    private KeyFixture() {
        // fixture class
    }

    public static Key aKey() {
        return Key.from("key");
    }

    public static Key aKeyWithExpiration(Instant expiration) {
        return Key.from("key", expiration);
    }
}