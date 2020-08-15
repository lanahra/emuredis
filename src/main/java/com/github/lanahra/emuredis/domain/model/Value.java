package com.github.lanahra.emuredis.domain.model;

import java.time.Instant;

public abstract class Value {

    protected final Instant expiration;

    protected Value() {
        this.expiration = null;
    }

    protected Value(Instant expiration) {
        this.expiration = expiration;
    }

    public boolean isExpired(Clock clock) {
        return expiration != null && clock.now().isAfter(expiration);
    }
}
