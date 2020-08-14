package com.github.lanahra.emuredis.domain.model.string;

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
}