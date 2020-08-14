package com.github.lanahra.emuredis.domain.model;

import static com.github.lanahra.emuredis.domain.model.KeyFixture.aKey;
import static com.github.lanahra.emuredis.domain.model.KeyFixture.aKeyWithExpiration;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.testing.EqualsTester;
import java.time.Duration;
import java.time.Instant;
import org.junit.Test;

public class KeyTest {

    @Test
    public void fulfillsEqualsAndHashCodeContract() {
        Instant expiration = Instant.now();
        new EqualsTester()
                .addEqualityGroup(Key.from(""), Key.from(""))
                .addEqualityGroup(Key.from("key"), Key.from("key"))
                .addEqualityGroup(Key.from("KEY"), Key.from("KEY"))
                .addEqualityGroup(Key.from("key", expiration), Key.from("key", expiration))
                .testEquals();
    }

    @Test
    public void keyWithoutExpirationIsNeverExpired() {
        Key key = aKey();

        when(clock.now()).thenReturn(Instant.now());

        assertThat(key.isExpired(clock), is(false));
    }

    @Test
    public void keyWithExpirationIsExpired() {
        Instant now = Instant.now();
        Key key = aKeyWithExpiration(now.minus(Duration.ofSeconds(1)));

        when(clock.now()).thenReturn(Instant.now());

        assertThat(key.isExpired(clock), is(true));
    }

    @Test
    public void keyWithExpirationIsNotExpired() {
        Instant now = Instant.now();
        Key key = aKeyWithExpiration(now.plus(Duration.ofSeconds(1)));

        when(clock.now()).thenReturn(Instant.now());

        assertThat(key.isExpired(clock), is(false));
    }

    private final Clock clock = mock(Clock.class);
}
