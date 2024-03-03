package edu.java.bot.api.exception.base;

public class ConflictException extends ApiException {
    public ConflictException(String description) {
        super(description);
    }

    public ConflictException(String description, String errorMessage, Throwable err) {
        super(description, errorMessage, err);
    }
}
