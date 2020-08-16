package com.github.lanahra.emuredis.application.string;

import com.github.lanahra.emuredis.domain.model.Key;
import java.util.Objects;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class IsASetCommand extends TypeSafeMatcher<SetCommand> {

    private final String key;
    private final String value;
    private final Long expiration;

    private IsASetCommand(String key, String value, Long expiration) {
        this.key = key;
        this.value = value;
        this.expiration = expiration;
    }

    public static IsASetCommand aSetCommandWith(String key, String value) {
        return new IsASetCommand(key, value, null);
    }

    public static IsASetCommand aSetCommandWith(String key, String value, Long expiration) {
        return new IsASetCommand(key, value, expiration);
    }

    @Override
    protected boolean matchesSafely(SetCommand item) {
        return item.key().equals(Key.from(key))
                && item.value().equals(value)
                && Objects.equals(item.expirationInSeconds(), expiration);
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("a set command with key ")
                .appendValue(key)
                .appendText(" and value ")
                .appendValue(value);

        if (expiration != null) {
            description
                    .appendText(" and expiration of ")
                    .appendValue(expiration)
                    .appendText(" seconds");
        }
    }
}
