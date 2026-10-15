package com.cowboy.enigma.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Turns bad requests into 400 with a single body shape: {"error": "..."}. */
@RestControllerAdvice
public class ApiExceptionHandler {

    /** Body of every error response. */
    public record ErrorResponse(String error) {
    }

    /** Validation failures, from the controller or from the core classes. */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidInput(IllegalArgumentException e) {
        return new ErrorResponse(e.getMessage());
    }

    /** Body that is not valid JSON (or not the expected shape). */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse malformedRequest(HttpMessageNotReadableException e) {
        return new ErrorResponse("Malformed request");
    }
}
