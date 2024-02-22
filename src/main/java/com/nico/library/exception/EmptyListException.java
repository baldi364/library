package com.nico.library.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class EmptyListException extends RuntimeException
{
    private final String elementNotFound;

    public EmptyListException(String elementNotFound)
    {
        super(String.format("No %s found", elementNotFound));
        this.elementNotFound = elementNotFound;
    }
}
