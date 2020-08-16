package com.github.lanahra.emuredis.application.string;

import com.github.lanahra.emuredis.domain.model.Key;

public class SetCommand {

    private final String key;
    private final String value;
    private final Long expirationInSeconds;

    public SetCommand(String key, String value) {
        this(key, value, null);
    }

    public SetCommand(String key, String value, Long expirationInSeconds) {
        this.key = key;
        this.value = value;
        this.expirationInSeconds = expirationInSeconds;
    }

    public Key key() {
        return Key.from(key);
    }

    public String value() {
        return value;
    }

    public boolean hasExpiration() {
        return expirationInSeconds != null;
    }

    public Long expirationInSeconds() {
        return expirationInSeconds;
    }
}
