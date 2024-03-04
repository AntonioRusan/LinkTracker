package edu.java.api.exception;

public enum ApiError {
    TG_CHAT_NOT_FOUND("Чат не найден"),
    LINK_NOT_FOUND("Ссылка не найдена"),
    LINK_ALREADY_ADDED("Повторное добавление ссылки"),
    TG_CHAT_ALREADY_REGISTERED("Повторная регистрация чата");

    private final String message;

    ApiError(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }

}
