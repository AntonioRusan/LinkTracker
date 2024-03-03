package edu.java.bot.api.exception.base;

public abstract class ApiException extends RuntimeException {
    private final String description;
    public String getDescription()
    {
        return description;
    }
    public ApiException(String description, String errorMessage, Throwable err) {
        super(errorMessage, err);
        this.description = description;
    }
}