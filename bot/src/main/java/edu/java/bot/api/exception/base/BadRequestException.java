package edu.java.bot.api.exception.base;

public class BadRequestException extends ApiException {
    public BadRequestException(String description, String errorMessage, Throwable err) {
        super(description, errorMessage, err);
    }
}
