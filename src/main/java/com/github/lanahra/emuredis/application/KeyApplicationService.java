package com.github.lanahra.emuredis.application;

import com.github.lanahra.emuredis.domain.model.Key;
import com.github.lanahra.emuredis.domain.model.KeyValueRepository;
import java.util.Arrays;

public class KeyApplicationService {

    private final KeyValueRepository repository;
    private final Transaction transaction;

    public KeyApplicationService(KeyValueRepository repository, Transaction transaction) {
        this.repository = repository;
        this.transaction = transaction;
    }

    public int dbSize() {
        return transaction.execute(repository::size);
    }

    public int delete(String... keys) {
        return transaction.execute(() -> repository.delete(keysFrom(keys)));
    }

    private Key[] keysFrom(String... keys) {
        return Arrays.stream(keys).map(Key::from).toArray(Key[]::new);
    }
}
