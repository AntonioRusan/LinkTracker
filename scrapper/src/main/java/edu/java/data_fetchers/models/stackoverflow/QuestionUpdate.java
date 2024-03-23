package edu.java.data_fetchers.models.stackoverflow;

import java.time.OffsetDateTime;

public record QuestionUpdate(
    String name,
    String htmlUrl,
    OffsetDateTime updatedAt
) {

    public String toString() {
        return String.format(
            "Обновление вопроса: %s\nДата: %s\nСсылка: %s\n",
            name,
            htmlUrl,
            updatedAt
        );
    }
}
