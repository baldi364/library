package com.nico.library.exceptions.custom;

import lombok.Getter;

import java.util.Set;

@Getter
public class EmptyAuthoritiesException extends RuntimeException {

    private final Set<String> authorities;

    public EmptyAuthoritiesException(Set<String> authorities) {
        super(String.format("No authorities found with name %s", authorities));
        this.authorities = authorities;
    }
}
