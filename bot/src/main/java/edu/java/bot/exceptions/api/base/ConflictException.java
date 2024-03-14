package edu.java.bot.exceptions.api.base;

import edu.java.bot.exceptions.api.ApiError;

public class ConflictException extends ApiException {
    public ConflictException(String description) {
        super(description);
    }

    public ConflictException(ApiError error) {
        super(error.message());
    }

    public ConflictException(String description, String errorMessage, Throwable err) {
        super(description, errorMessage, err);
    }
}
