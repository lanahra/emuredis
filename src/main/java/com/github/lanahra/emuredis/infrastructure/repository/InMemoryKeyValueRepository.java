package com.github.lanahra.emuredis.infrastructure.repository;

import com.github.lanahra.emuredis.domain.model.Clock;
import com.github.lanahra.emuredis.domain.model.Key;
import com.github.lanahra.emuredis.domain.model.KeyNotFoundException;
import com.github.lanahra.emuredis.domain.model.KeyValueRepository;
import com.github.lanahra.emuredis.domain.model.Value;
import com.github.lanahra.emuredis.domain.model.ValueWrongTypeException;
import com.github.lanahra.emuredis.domain.model.sortedset.SortedSetValue;
import com.github.lanahra.emuredis.domain.model.string.StringValue;
import java.util.HashMap;
import java.util.Map;

public class InMemoryKeyValueRepository implements KeyValueRepository {

    private final Map<Key, Value> map;
    private final Clock clock;

    public InMemoryKeyValueRepository(Clock clock) {
        this.map = new HashMap<>();
        this.clock = clock;
    }

    @Override
    public void add(Key key, Value value) {
        map.put(key, value);
    }

    @Override
    public StringValue stringFor(Key key) {
        Value value = valueFor(key);
        if (value instanceof StringValue) {
            return (StringValue) value;
        } else {
            throw new ValueWrongTypeException();
        }
    }

    @Override
    public SortedSetValue sortedSetFor(Key key) {
        Value value = valueFor(key);
        if (value instanceof SortedSetValue) {
            return (SortedSetValue) value;
        } else {
            throw new ValueWrongTypeException();
        }
    }

    private Value valueFor(Key key) {
        Value value = map.get(key);
        if (value == null || value.isExpired(clock)) {
            map.remove(key);
            throw new KeyNotFoundException();
        }
        return value;
    }

    @Override
    public int delete(Key... keys) {
        int deleted = 0;
        for (Key key : keys) {
            Value value = map.remove(key);
            if (value != null) {
                deleted++;
            }
        }
        return deleted;
    }

    @Override
    public int size() {
        return map.size();
    }
}
