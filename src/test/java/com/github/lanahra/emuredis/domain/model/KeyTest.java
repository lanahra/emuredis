package com.github.lanahra.emuredis.domain.model;

import com.google.common.testing.EqualsTester;
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
                .testEquals();
    }
}
