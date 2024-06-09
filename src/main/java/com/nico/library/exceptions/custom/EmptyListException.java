package com.nico.library.exceptions.custom;

import lombok.Getter;

@Getter
public class EmptyListException extends RuntimeException {
    private final String elementNotFound;
    private final String username;

    public EmptyListException(String elementNotFound) {
        super(String.format("No %s found", elementNotFound));
        this.elementNotFound = elementNotFound;
        this.username = null;
    }

    public EmptyListException(String elementNotFound, String username) {
        super(String.format("No %s found for user %s", elementNotFound, username));
        this.elementNotFound = elementNotFound;
        this.username = username;
    }


}
