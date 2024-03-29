package ee.kaido.webshop.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentTypeMismatchException e) {
        ExceptionResponse response = getExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(EmailExistsException e) {
        ExceptionResponse response = getExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(NoSuchElementException e) {
        ExceptionResponse response = getExceptionResponse(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(HttpRequestMethodNotSupportedException e) {
        ExceptionResponse response = getExceptionResponse(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(PersonNotFoundException e) {
        ExceptionResponse response = getExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(PersonExistsException e) {
        ExceptionResponse response = getExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    private static ExceptionResponse getExceptionResponse(HttpStatus methodNotAllowed, String message) {
        ExceptionResponse response = new ExceptionResponse();
        response.setHttpStatus(methodNotAllowed);
        response.setHttpStatusCode(methodNotAllowed.value());
        response.setTimestamp(new Date());
        response.setMessage(message);
        return response;
    }
}
