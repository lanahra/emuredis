package com.github.lanahra.emuredis.domain.model.string;

import java.time.Instant;

public class StringValueFixture {

    private StringValueFixture() {
        // fixture class
    }

    public static StringValue aStringValue() {
        return StringValue.from("value");
    }

    public static StringValue aNumericStringValue() {
        return StringValue.from("10");
    }

    public static StringValue aStringValueWithExpiration(Instant expiration) {
        return StringValue.from("value", expiration);
    }
}