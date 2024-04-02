package edu.java.exceptions.api;

public class TgChatApiException extends RuntimeException {
    public TgChatApiException(String message) {
        super(message);
    }

    public String getDescription() {
        return "Ошибка на стороне сервера TgChatApi!";
    }
}
