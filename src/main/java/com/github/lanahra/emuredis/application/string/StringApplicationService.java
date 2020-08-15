package com.github.lanahra.emuredis.application.string;

import com.github.lanahra.emuredis.application.Transaction;
import com.github.lanahra.emuredis.domain.model.Clock;
import com.github.lanahra.emuredis.domain.model.Key;
import com.github.lanahra.emuredis.domain.model.KeyNotFoundException;
import com.github.lanahra.emuredis.domain.model.KeyValueRepository;
import com.github.lanahra.emuredis.domain.model.string.StringValue;
import java.time.Duration;
import java.time.Instant;

public class StringApplicationService {

    private final Clock clock;
    private final KeyValueRepository repository;
    private final Transaction transaction;

    public StringApplicationService(
            Clock clock, KeyValueRepository repository, Transaction transaction) {
        this.clock = clock;
        this.repository = repository;
        this.transaction = transaction;
    }

    public void setValue(SetCommand command) {
        transaction.execute(
                () -> {
                    Key key = command.key();
                    StringValue value = valueFrom(command);
                    repository.add(key, value);
                });
    }

    private StringValue valueFrom(SetCommand command) {
        return command.hasExpiration()
                ? StringValue.from(command.value(), expirationFor(command.expirationInSeconds()))
                : StringValue.from(command.value());
    }

    private Instant expirationFor(Long expirationInSeconds) {
        return clock.now().plus(Duration.ofSeconds(expirationInSeconds));
    }

    public StringValue getValueOf(String key) {
        return transaction.execute(() -> repository.stringFor(Key.from(key)));
    }

    public long incrementValueOf(String key) {
        return transaction.execute(
                () -> {
                    StringValue value = stringOf(Key.from(key));
                    value.increment();
                    return value.asLong();
                });
    }

    private StringValue stringOf(Key key) {
        try {
            return repository.stringFor(key);
        } catch (KeyNotFoundException e) {
            StringValue value = StringValue.zero();
            repository.add(key, value);
            return value;
        }
    }
}
