package edu.java.bot.api.exceptions;

public enum ApiError {
    CHATS_NOT_FOUND("Чаты не найдены"),
    LINK_NOT_FOUND("Ссылка не найдена");

    private final String message;

    ApiError(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }

}
