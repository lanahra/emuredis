package com.github.lanahra.emuredis.application;

import com.github.lanahra.emuredis.domain.model.string.StringValue;

public class SetCommand {

    private final String key;
    private final String value;
    private final Long expirationInSeconds;

    public SetCommand(String key, String value, Long expirationInSeconds) {
        this.key = key;
        this.value = value;
        this.expirationInSeconds = expirationInSeconds;
    }

    public String key() {
        return key;
    }

    public StringValue value() {
        return StringValue.from(value);
    }

    public boolean hasExpiration() {
        return expirationInSeconds != null;
    }

    public Long expirationInSeconds() {
        return expirationInSeconds;
    }
}
