package com.nico.library.exceptions;

import com.nico.library.exceptions.custom.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ExceptionManagement
{
    @ExceptionHandler({
            EmptyListException.class,
            ResourceNotFoundException.class
    })
    public ResponseEntity<Object> NotFoundExceptionManagement(RuntimeException ex, HttpServletRequest request)
    {
        HttpStatus notFound = NOT_FOUND;
        ApiException apiException = new ApiException(
                ZonedDateTime.now(ZoneId.of("Z")),
                ex.getMessage(),
                notFound.toString(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(apiException, notFound);
    }
    @ExceptionHandler({
            BadRequestException.class,
            BookAlreadyPresentException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<Object> BadRequestExceptionManagement(RuntimeException ex, HttpServletRequest request){
        HttpStatus badRequest = BAD_REQUEST;
        ApiException apiException = new ApiException(
                ZonedDateTime.now(ZoneId.of("Z")),
                ex.getMessage(),
                badRequest.toString(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<String> handleArgumentNotValidValue(MethodArgumentNotValidException ex) {

        BindingResult bindingResults = ex.getBindingResult();

        List<String> errors = bindingResults.getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();

        return new ResponseEntity<>(errors.toString(), BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<?> constraintViolationExceptionManagement(ConstraintViolationException ex)
    {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

        //qui mappo ciascuna "ConstraintViolation" all'attributo "message", che conterrà il messaggio di errore
        List<String> errors = violations.stream() //creo uno stream di oggetti "ConstraintViolation"
                .map(ConstraintViolation::getMessage)
                .toList();  //qui i messaggi di errore vengono raccolti e trasformati in una lista di stringhe

        return new ResponseEntity<>(errors, BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<?> handleDataIntegrityViolationManagement(DataIntegrityViolationException ex) {
        String errorMessage = "Errore di integrità dei dati: " + ex.getMostSpecificCause().getMessage();
        return new ResponseEntity<>(Collections.singletonMap("error", errorMessage), BAD_REQUEST);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<?> BadCredentialsExceptionManagement(BadCredentialsException ex)
    {
        return new ResponseEntity<>(ex.getMessage(), UNAUTHORIZED);
    }
}
