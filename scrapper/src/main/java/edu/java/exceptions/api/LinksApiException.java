package edu.java.exceptions.api;

public class LinksApiException extends RuntimeException {
    public LinksApiException(String message) {
        super(message);
    }

    public String getDescription() {
        return "Ошибка на стороне сервера LinksApi!";
    }
}
