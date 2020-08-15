package com.github.lanahra.emuredis.domain.model;

import java.time.Instant;

public class ValueFixture {

    private ValueFixture() {
        // fixture class
    }

    public static Value aValue() {
        return new SimpleValue();
    }

    public static Value aValueWithExpiration(Instant expiration) {
        return new SimpleValue(expiration);
    }

    static class SimpleValue extends Value {

        public SimpleValue() {
            super();
        }

        public SimpleValue(Instant expiration) {
            super(expiration);
        }
    }
}
