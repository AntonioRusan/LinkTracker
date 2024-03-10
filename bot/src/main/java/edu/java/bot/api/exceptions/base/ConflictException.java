package edu.java.bot.api.exceptions.base;

import edu.java.bot.api.exceptions.ApiError;

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
