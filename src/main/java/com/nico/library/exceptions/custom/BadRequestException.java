package com.nico.library.exceptions.custom;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException{
    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }
}

