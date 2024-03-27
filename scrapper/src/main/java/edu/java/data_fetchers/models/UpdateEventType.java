package edu.java.data_fetchers.models;

public enum UpdateEventType {

    NEW_PULL_REQUEST("Добавлен новый Pull Request"),
    REPOSITORY_UPDATE("Обновление репозитория"),
    QUESTION_UPDATE("Обновление вопроса"),
    NEW_ANSWER("Добавлен новый ответ");
    private final String description;

    UpdateEventType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
