package com.github.lanahra.emuredis.domain.model.string;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.testing.EqualsTester;
import org.junit.Test;

public class StringValueTest {

    @Test
    public void fulfillsEqualsAndHashCodeContract() {
        new EqualsTester()
                .addEqualityGroup(StringValue.zero(), StringValue.zero())
                .addEqualityGroup(StringValue.from(""), StringValue.from(""))
                .addEqualityGroup(StringValue.from("value"), StringValue.from("value"))
                .addEqualityGroup(StringValue.from("VALUE"), StringValue.from("VALUE"))
                .testEquals();
    }

    @Test
    public void incrementsStringValueAsIntegerByOne() {
        StringValue value = StringValue.zero();

        long firstIncrement = value.increment();
        long secondIncrement = value.increment();

        assertThat(firstIncrement, is(1L));
        assertThat(secondIncrement, is(2L));
    }

    @Test(expected = IntegerRepresentationException.class)
    public void throwsExceptionForIncrementingNonIntegerStringValue() {
        StringValue value = StringValue.from("non-integer");

        value.increment();
    }

    @Test(expected = IncrementOverflowException.class)
    public void throwsExceptionForIncrementBeyondMaximumPossibleValue() {
        StringValue value = StringValue.from(String.valueOf(Long.MAX_VALUE));

        value.increment();
    }
}