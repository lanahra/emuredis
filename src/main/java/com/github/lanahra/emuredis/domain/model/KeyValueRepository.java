package com.github.lanahra.emuredis.domain.model;

import com.github.lanahra.emuredis.domain.model.sortedset.SortedSetValue;
import com.github.lanahra.emuredis.domain.model.string.StringValue;

public interface KeyValueRepository {

    void add(Key key, Value value);

    StringValue stringFor(Key key);

    SortedSetValue sortedSetFor(Key key);

    int delete(Key... keys);

    int size();
}
