package edu.java.api.exception.base;

public class NotFoundException extends ApiException{
    public NotFoundException(String description,  String errorMessage, Throwable err) {
        super(description, errorMessage, err);
    }
}
