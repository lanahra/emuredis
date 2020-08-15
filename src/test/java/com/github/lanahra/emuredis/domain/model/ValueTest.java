package com.github.lanahra.emuredis.domain.model;

import static com.github.lanahra.emuredis.domain.model.ValueFixture.aValue;
import static com.github.lanahra.emuredis.domain.model.ValueFixture.aValueWithExpiration;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import org.junit.Test;

public class ValueTest {

    @Test
    public void valueWithoutExpirationIsNeverExpired() {
        Value value = aValue();

        when(clock.now()).thenReturn(Instant.now());

        assertThat(value.isExpired(clock), is(false));
    }

    @Test
    public void keyWithExpirationIsExpired() {
        Instant now = Instant.now();
        Value value = aValueWithExpiration(now.minus(Duration.ofSeconds(1)));

        when(clock.now()).thenReturn(Instant.now());

        assertThat(value.isExpired(clock), is(true));
    }

    @Test
    public void keyWithExpirationIsNotExpired() {
        Instant now = Instant.now();
        Value value = aValueWithExpiration(now.plus(Duration.ofSeconds(1)));

        when(clock.now()).thenReturn(Instant.now());

        assertThat(value.isExpired(clock), is(false));
    }

    private final Clock clock = mock(Clock.class);
}
