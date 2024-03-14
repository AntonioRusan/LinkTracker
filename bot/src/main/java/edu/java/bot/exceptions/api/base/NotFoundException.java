package edu.java.bot.exceptions.api.base;

import edu.java.bot.exceptions.api.ApiError;

public class NotFoundException extends ApiException {
    public NotFoundException(String description) {
        super(description);
    }

    public NotFoundException(ApiError error) {
        super(error.message());
    }

    public NotFoundException(String description, String errorMessage, Throwable err) {
        super(description, errorMessage, err);
    }
}
