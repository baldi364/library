package com.nico.library.exceptions.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

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
