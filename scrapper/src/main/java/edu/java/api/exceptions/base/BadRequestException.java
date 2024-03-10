package edu.java.api.exceptions.base;

import edu.java.api.exceptions.ApiError;

public class BadRequestException extends ApiException {
    public BadRequestException(String description) {
        super(description);
    }

    public BadRequestException(ApiError error) {
        super(error.message());
    }

    public BadRequestException(String description, String errorMessage, Throwable err) {
        super(description, errorMessage, err);
    }
}
