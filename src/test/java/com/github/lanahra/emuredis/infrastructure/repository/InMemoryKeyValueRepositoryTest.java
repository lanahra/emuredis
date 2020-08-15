package com.github.lanahra.emuredis.infrastructure.repository;

import static com.github.lanahra.emuredis.domain.model.KeyFixture.aKey;
import static com.github.lanahra.emuredis.domain.model.sortedset.SortedSetValueFixture.aSortedSetValue;
import static com.github.lanahra.emuredis.domain.model.string.StringValueFixture.aStringValue;
import static com.github.lanahra.emuredis.domain.model.string.StringValueFixture.aStringValueWithExpiration;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.lanahra.emuredis.domain.model.Clock;
import com.github.lanahra.emuredis.domain.model.Key;
import com.github.lanahra.emuredis.domain.model.KeyNotFoundException;
import com.github.lanahra.emuredis.domain.model.KeyValueRepository;
import com.github.lanahra.emuredis.domain.model.ValueWrongTypeException;
import com.github.lanahra.emuredis.domain.model.sortedset.SortedSetValue;
import com.github.lanahra.emuredis.domain.model.string.StringValue;
import java.time.Duration;
import java.time.Instant;
import org.junit.Test;

public class InMemoryKeyValueRepositoryTest {

    @Test
    public void addsValueToRepository() {
        repository.add(Key.from("a"), aStringValue());
        repository.add(Key.from("b"), aSortedSetValue());

        StringValue stringValue = repository.stringFor(Key.from("a"));
        SortedSetValue sortedSetValue = repository.sortedSetFor(Key.from("b"));

        assertThat(stringValue, is(aStringValue()));
        assertThat(sortedSetValue, is(aSortedSetValue()));
    }

    @Test
    public void retrievesNotYetExpiredValueFromRepository() {
        Instant now = Instant.now();
        repository.add(aKey(), aStringValueWithExpiration(now.plus(Duration.ofSeconds(1))));

        when(clock.now()).thenReturn(now);

        StringValue value = repository.stringFor(aKey());
        assertThat(value, is(aStringValueWithExpiration(now.plus(Duration.ofSeconds(1)))));
    }

    @Test(expected = KeyNotFoundException.class)
    public void throwsExceptionForRetrievingExpiredValueFromRepository() {
        Instant now = Instant.now();
        repository.add(aKey(), aStringValueWithExpiration(now.minus(Duration.ofSeconds(1))));

        when(clock.now()).thenReturn(now);

        repository.stringFor(aKey());
    }

    @Test(expected = KeyNotFoundException.class)
    public void throwsExceptionForRetrievingNonExistentKeyFromRepository() {
        repository.stringFor(aKey());
    }

    @Test(expected = ValueWrongTypeException.class)
    public void throwsExceptionForRetrievingStringValueOfKeyOfWrongTypeFromRepository() {
        repository.add(aKey(), aSortedSetValue());
        repository.stringFor(aKey());
    }

    @Test(expected = ValueWrongTypeException.class)
    public void throwsExceptionForRetrievingSortedSetValueOfKeyOfWrongTypeFromRepository() {
        repository.add(aKey(), aStringValue());
        repository.sortedSetFor(aKey());
    }

    @Test
    public void retrievesRepositorySize() {
        repository.add(Key.from("a"), aStringValue());
        repository.add(Key.from("b"), aSortedSetValue());

        assertThat(repository.size(), is(2));
    }

    @Test
    public void deletesKeysFromRepository() {
        repository.add(Key.from("a"), aStringValue());
        repository.add(Key.from("b"), aSortedSetValue());

        repository.delete(Key.from("a"), Key.from("b"));

        assertThat(repository.size(), is(0));
    }

    private final Clock clock = mock(Clock.class);
    private final KeyValueRepository repository = new InMemoryKeyValueRepository(clock);
}
