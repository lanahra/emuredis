package com.github.lanahra.emuredis.application;

import com.github.lanahra.emuredis.domain.model.Clock;
import com.github.lanahra.emuredis.domain.model.Key;
import com.github.lanahra.emuredis.domain.model.KeyValueRepository;
import com.github.lanahra.emuredis.domain.model.string.StringValue;
import java.time.Duration;
import java.time.Instant;

public class StringApplicationService {

    private final Clock clock;
    private final KeyValueRepository repository;

    public StringApplicationService(Clock clock, KeyValueRepository repository) {
        this.clock = clock;
        this.repository = repository;
    }

    public void set(SetCommand command) {
        Key key = keyFrom(command);
        StringValue value = command.value();
        repository.add(key, value);
    }

    private Key keyFrom(SetCommand command) {
        return command.hasExpiration()
                ? Key.from(command.key(), expirationFor(command.expirationInSeconds()))
                : Key.from(command.key());
    }

    private Instant expirationFor(Long expirationInSeconds) {
        return clock.now().plus(Duration.ofSeconds(expirationInSeconds));
    }

    public StringValue get(String key) {
        return repository.stringFor(Key.from(key));
    }

    public long increment(String key) {
        StringValue value = repository.stringFor(Key.from(key));
        value.increment();
        return value.asLong();
    }
}
