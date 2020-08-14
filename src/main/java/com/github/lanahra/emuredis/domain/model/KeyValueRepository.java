package com.github.lanahra.emuredis.domain.model;

import com.github.lanahra.emuredis.domain.model.string.StringValue;

public interface KeyValueRepository {

    int size();

    int delete(Key... keys);

    StringValue stringFor(Key key);

    void add(Key key, StringValue value);
}
