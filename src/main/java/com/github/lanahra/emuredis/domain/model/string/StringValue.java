package com.github.lanahra.emuredis.domain.model.string;

import com.github.lanahra.emuredis.domain.model.Value;
import java.time.Instant;
import java.util.Objects;

public class StringValue extends Value {

    private String value;

    private StringValue(String value) {
        super();
        this.value = value;
    }

    private StringValue(String value, Instant expiration) {
        super(expiration);
        this.value = value;
    }

    public static StringValue zero() {
        return new StringValue("0");
    }

    public static StringValue from(String value) {
        return new StringValue(value);
    }

    public static StringValue from(String value, Instant expiration) {
        return new StringValue(value, expiration);
    }

    public long increment() {
        long value = asLong();
        if (value == Long.MAX_VALUE) {
            throw new IncrementOverflowException();
        }
        this.value = String.valueOf(++value);
        return value;
    }

    public long asLong() {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new IntegerRepresentationException(e);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other != null
                && getClass().equals(other.getClass())
                && equalsCasted((StringValue) other);
    }

    private boolean equalsCasted(StringValue other) {
        return value.equals(other.value) && Objects.equals(expiration, other.expiration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, expiration);
    }
}
