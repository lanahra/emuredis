package com.github.lanahra.emuredis.application.sortedset;

import com.github.lanahra.emuredis.application.Transaction;
import com.github.lanahra.emuredis.domain.model.Key;
import com.github.lanahra.emuredis.domain.model.KeyNotFoundException;
import com.github.lanahra.emuredis.domain.model.KeyValueRepository;
import com.github.lanahra.emuredis.domain.model.sortedset.Member;
import com.github.lanahra.emuredis.domain.model.sortedset.SortedSetValue;
import java.util.List;

public class SortedSetApplicationService {

    private final KeyValueRepository repository;
    private final Transaction transaction;

    public SortedSetApplicationService(KeyValueRepository repository, Transaction transaction) {
        this.repository = repository;
        this.transaction = transaction;
    }

    public int addMember(AddCommand command) {
        return transaction.supply(
                () -> {
                    SortedSetValue value = sortedSetOf(command.key());
                    return value.add(command.member());
                });
    }

    private SortedSetValue sortedSetOf(Key key) {
        try {
            return repository.sortedSetFor(key);
        } catch (KeyNotFoundException e) {
            SortedSetValue value = SortedSetValue.empty();
            repository.add(key, value);
            return value;
        }
    }

    public int cardinalityOfSortedSetOf(String key) {
        return transaction.supply(
                () -> {
                    try {
                        SortedSetValue value = repository.sortedSetFor(Key.from(key));
                        return value.cardinality();
                    } catch (KeyNotFoundException e) {
                        return 0;
                    }
                });
    }

    public int rankInSortedSet(String key, String value) {
        return transaction.supply(
                () -> {
                    SortedSetValue sortedSetValue = repository.sortedSetFor(Key.from(key));
                    return sortedSetValue.rank(Member.from(value));
                });
    }

    public List<Member> rangeFromSortedSet(RangeCommand command) {
        return transaction.supply(
                () -> {
                    SortedSetValue sortedSetValue = repository.sortedSetFor(command.key());
                    return sortedSetValue.range(command.start(), command.stop());
                });
    }
}
