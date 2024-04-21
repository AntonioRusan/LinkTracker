package edu.java.handlers;

import api.models.ApiErrorResponse;
import edu.java.exceptions.api.LinksApiException;
import edu.java.exceptions.api.TgChatApiException;
import edu.java.exceptions.api.base.BadRequestException;
import edu.java.exceptions.api.base.ConflictException;
import edu.java.exceptions.api.base.NotFoundException;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(LinksApiException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiErrorResponse> linksApiExceptionHandle(LinksApiException exception) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiErrorResponse(
                    exception.getDescription(),
                    String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    exception.getClass().getName(),
                    exception.getMessage(),
                    Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList()
                )
            );
    }

    @ExceptionHandler(TgChatApiException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiErrorResponse> tgChatApiExceptionHandle(TgChatApiException exception) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiErrorResponse(
                    exception.getDescription(),
                    String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    exception.getClass().getName(),
                    exception.getMessage(),
                    Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList()
                )
            );
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> badRequestExceptionHandle(BadRequestException exception) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ApiErrorResponse(
                    exception.getDescription(),
                    String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    exception.getClass().getName(),
                    exception.getMessage(),
                    Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList()
                )
            );
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiErrorResponse> notFoundExceptionHandle(NotFoundException exception) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ApiErrorResponse(
                    exception.getDescription(),
                    String.valueOf(HttpStatus.NOT_FOUND.value()),
                    exception.getClass().getName(),
                    exception.getMessage(),
                    Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList()
                )
            );
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseEntity<ApiErrorResponse> conflictExceptionHandle(ConflictException exception) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ApiErrorResponse(
                    exception.getDescription(),
                    String.valueOf(HttpStatus.CONFLICT.value()),
                    exception.getClass().getName(),
                    exception.getMessage(),
                    Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList()
                )
            );
    }

}
