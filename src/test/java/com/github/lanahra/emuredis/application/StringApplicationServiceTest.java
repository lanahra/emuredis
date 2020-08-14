package com.github.lanahra.emuredis.application;

import static com.github.lanahra.emuredis.application.SetCommandFixture.aSetCommand;
import static com.github.lanahra.emuredis.application.SetCommandFixture.aSetCommandWithExpiration;
import static com.github.lanahra.emuredis.domain.model.KeyFixture.aKey;
import static com.github.lanahra.emuredis.domain.model.string.StringValueFixture.aNumericStringValue;
import static com.github.lanahra.emuredis.domain.model.string.StringValueFixture.aStringValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.lanahra.emuredis.domain.model.Clock;
import com.github.lanahra.emuredis.domain.model.Key;
import com.github.lanahra.emuredis.domain.model.KeyValueRepository;
import com.github.lanahra.emuredis.domain.model.string.StringValue;
import java.time.Duration;
import java.time.Instant;
import org.junit.Test;

public class StringApplicationServiceTest {

    @Test
    public void addsStringValueKeyToTheRepository() {
        SetCommand command = aSetCommand();

        service.set(command);

        verify(repository).add(Key.from("key"), StringValue.from("value"));
    }

    @Test
    public void addsStringValueKeyWithExpirationToTheRepository() {
        SetCommand command = aSetCommandWithExpiration();
        Instant now = Instant.now();

        when(clock.now()).thenReturn(now);

        service.set(command);

        verify(repository)
                .add(Key.from("key", now.plus(Duration.ofSeconds(10))), StringValue.from("value"));
    }

    @Test
    public void retrievesStringValueForKeyFromRepository() {
        StringValue value = aStringValue();

        when(repository.stringFor(aKey())).thenReturn(value);

        assertThat(service.get("key"), is(value));
    }

    @Test
    public void incrementsKeyInRepository() {
        when(repository.stringFor(aKey())).thenReturn(aNumericStringValue());

        assertThat(service.increment("key"), is(11L));
    }

    private final Clock clock = mock(Clock.class);
    private final KeyValueRepository repository = mock(KeyValueRepository.class);
    private final StringApplicationService service =
            new StringApplicationService(clock, repository);
}
