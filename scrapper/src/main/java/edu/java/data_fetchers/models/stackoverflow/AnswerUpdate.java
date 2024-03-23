package edu.java.data_fetchers.models.stackoverflow;

import java.time.OffsetDateTime;

public record AnswerUpdate(
    String url,
    OffsetDateTime createdAt
) {
    public String toString() {
        return String.format(
            "Добавлен ответ на вопрос:\nСсылка: %s\nДата: %s\n",
            url,
            createdAt
        );
    }
}
