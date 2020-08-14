package com.github.lanahra.emuredis.domain.model;

import java.time.Instant;
import java.util.Objects;

public class Key {

    private final String name;
    private final Instant expiration;

    private Key(String name, Instant expiration) {
        this.name = name;
        this.expiration = expiration;
    }

    public static Key from(String name) {
        return new Key(name, null);
    }

    public static Key from(String name, Instant expiration) {
        return new Key(name, expiration);
    }

    public boolean isExpired(Clock clock) {
        return expiration != null && clock.now().isAfter(expiration);
    }

    @Override
    public boolean equals(Object other) {
        return other != null && getClass().equals(other.getClass()) && equalsCasted((Key) other);
    }

    private boolean equalsCasted(Key other) {
        return name.equals(other.name) && Objects.equals(expiration, other.expiration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, expiration);
    }
}
