package com.nico.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionManagement
{
    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<?> ResourceNotFoundExceptionManagement(ResourceNotFoundException ex)
    {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EmptyListException.class})
    public ResponseEntity<?> EmptyListExceptionManagement(EmptyListException ex)
    {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<?> BadCredentialsExceptionManagement(BadCredentialsException ex)
    {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
