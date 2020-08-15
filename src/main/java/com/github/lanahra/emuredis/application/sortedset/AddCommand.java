package com.github.lanahra.emuredis.application.sortedset;

import com.github.lanahra.emuredis.domain.model.Key;
import com.github.lanahra.emuredis.domain.model.sortedset.Member;

public class AddCommand {

    private final String key;
    private final String value;
    private final double score;

    public AddCommand(String key, String value, double score) {
        this.key = key;
        this.value = value;
        this.score = score;
    }

    public Key key() {
        return Key.from(key);
    }

    public Member member() {
        return Member.from(value, score);
    }
}
