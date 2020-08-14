package com.github.lanahra.emuredis.application;

import com.github.lanahra.emuredis.domain.model.Key;
import com.github.lanahra.emuredis.domain.model.KeyValueRepository;
import java.util.Arrays;

public class KeyApplicationService {

    private final KeyValueRepository repository;

    public KeyApplicationService(KeyValueRepository repository) {
        this.repository = repository;
    }

    public int dbSize() {
        return repository.size();
    }

    public int delete(String... keys) {
        return repository.delete(keysFrom(keys));
    }

    private Key[] keysFrom(String... keys) {
        return Arrays.stream(keys).map(Key::from).toArray(Key[]::new);
    }
}
