package com.nico.library.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
@Getter
public class BadCredentialsException extends RuntimeException
{
    public BadCredentialsException()
    {
        super("Bad Credentials");
    }
}
