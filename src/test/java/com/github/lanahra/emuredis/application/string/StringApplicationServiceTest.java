package com.github.lanahra.emuredis.application.string;

import static com.github.lanahra.emuredis.application.string.SetCommandFixture.aSetCommand;
import static com.github.lanahra.emuredis.application.string.SetCommandFixture.aSetCommandWithExpiration;
import static com.github.lanahra.emuredis.domain.model.KeyFixture.aKey;
import static com.github.lanahra.emuredis.domain.model.string.StringValueFixture.aNumericStringValue;
import static com.github.lanahra.emuredis.domain.model.string.StringValueFixture.aStringValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.lanahra.emuredis.application.SimpleTransaction;
import com.github.lanahra.emuredis.application.Transaction;
import com.github.lanahra.emuredis.domain.model.Clock;
import com.github.lanahra.emuredis.domain.model.Key;
import com.github.lanahra.emuredis.domain.model.KeyNotFoundException;
import com.github.lanahra.emuredis.domain.model.KeyValueRepository;
import com.github.lanahra.emuredis.domain.model.string.StringValue;
import java.time.Duration;
import java.time.Instant;
import org.junit.Test;

public class StringApplicationServiceTest {

    @Test
    public void addsStringValueKeyToTheRepository() {
        SetCommand command = aSetCommand();

        service.setValue(command);

        verify(repository).add(Key.from("key"), StringValue.from("value"));
    }

    @Test
    public void addsStringValueKeyWithExpirationToTheRepository() {
        SetCommand command = aSetCommandWithExpiration();
        Instant now = Instant.now();

        when(clock.now()).thenReturn(now);

        service.setValue(command);

        verify(repository)
                .add(Key.from("key"), StringValue.from("value", now.plus(Duration.ofSeconds(10))));
    }

    @Test
    public void retrievesStringValueForKeyFromRepository() {
        StringValue value = aStringValue();

        when(repository.stringFor(aKey())).thenReturn(value);

        assertThat(service.getValueOf("key"), is(value));
    }

    @Test
    public void incrementsKeyInRepository() {
        when(repository.stringFor(aKey())).thenReturn(aNumericStringValue());

        assertThat(service.incrementValueOf("key"), is(11L));
    }

    @Test
    public void incrementsZeroValueAndAddsItToTheRepositoryForKeyNotFound() {
        Key key = aKey();
        when(repository.stringFor(key)).thenThrow(KeyNotFoundException.class);

        long result = service.incrementValueOf("key");

        verify(repository).add(key, StringValue.from("1"));
        assertThat(result, is(1L));
    }

    private final Clock clock = mock(Clock.class);
    private final KeyValueRepository repository = mock(KeyValueRepository.class);
    private final Transaction transaction = new SimpleTransaction();
    private final StringApplicationService service =
            new StringApplicationService(clock, repository, transaction);
}
