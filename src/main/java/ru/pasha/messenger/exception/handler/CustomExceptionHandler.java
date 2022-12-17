package ru.pasha.messenger.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.pasha.messenger.exception.*;
import ru.pasha.messenger.responseEntity.Response;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ChatNotFoundException.class})
    public ResponseEntity<Response> handleChatNotException(ChatNotFoundException e) {
        return new ResponseEntity<>(
                buildResponse(e.getMessage(), (short) HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(value = {PersonNotFoundException.class})
    public ResponseEntity<Response> handlePersonNotFoundException(PersonNotFoundException e) {
        return new ResponseEntity<>(
                buildResponse(e.getMessage(), (short) HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(value = {MessageNotFoundException.class})
    public ResponseEntity<Response> handleMessageNotFoundException(MessageNotFoundException e) {
        return new ResponseEntity<>(
                buildResponse(e.getMessage(), (short) HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(value = {ValidationException.class})
    public ResponseEntity<Response> handleValidationFailedException(ValidationException e) {
        return new ResponseEntity<>(
                buildResponse(e.getMessage(), (short) HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(value = {AlreadyExistsException.class})
    public ResponseEntity<Response> handleValidationFailedException(AlreadyExistsException e) {
        return new ResponseEntity<>(
                buildResponse(e.getMessage(), (short) HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST
        );
    }

    private Response buildResponse(String message, short code) {
        return new Response(message, code);
    }
}
