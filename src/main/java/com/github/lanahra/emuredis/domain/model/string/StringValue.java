package com.github.lanahra.emuredis.domain.model.string;

import com.github.lanahra.emuredis.domain.model.Value;
import java.util.Objects;

public class StringValue implements Value {

    public static final StringValue ZERO = new StringValue("0");

    private String value;

    private StringValue(String value) {
        this.value = value;
    }

    public static StringValue from(String value) {
        return new StringValue(value);
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
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
