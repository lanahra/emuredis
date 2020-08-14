package com.github.lanahra.emuredis.domain.model;

import java.time.Instant;

public interface Clock {

    Instant now();
}
