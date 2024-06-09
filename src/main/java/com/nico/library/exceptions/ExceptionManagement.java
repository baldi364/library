package com.nico.library.exceptions;

import com.nico.library.exceptions.custom.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ExceptionManagement
{
    @ExceptionHandler({
            EmptyListException.class,
            ResourceNotFoundException.class
    })
    public ResponseEntity<Object> NotFoundExceptionManagement(RuntimeException ex, HttpServletRequest request)
    {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                ZonedDateTime.now(ZoneId.of("Z")),
                ex.getMessage(),
                notFound.toString(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(apiException, notFound);
    }
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> BadRequestExceptionManagement(RuntimeException ex, HttpServletRequest request){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                ZonedDateTime.now(ZoneId.of("Z")),
                ex.getMessage(),
                badRequest.toString(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<?> BadCredentialsExceptionManagement(BadCredentialsException ex)
    {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
