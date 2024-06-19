package com.nico.library.exceptions.custom;

import lombok.Getter;

@Getter
public class BookAlreadyPresentException extends RuntimeException{

    private final Integer id;
    private final String username;

    public BookAlreadyPresentException(Integer id, String username) {
        super(String.format("Book with id '%d' already present in %s's library", id,  username));
        this.id = id;
        this.username = username;
    }
}
