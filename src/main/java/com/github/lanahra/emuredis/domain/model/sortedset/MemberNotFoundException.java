package com.github.lanahra.emuredis.domain.model.sortedset;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException(Member member) {
        super(String.format("Member not found in Sorted Set: '%s'", member.value()));
    }
}
