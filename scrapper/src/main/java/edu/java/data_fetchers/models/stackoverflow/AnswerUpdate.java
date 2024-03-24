package edu.java.data_fetchers.models.stackoverflow;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public record AnswerUpdate(
    String url,
    OffsetDateTime createdAt
) {
    public String toString() {
        String formattedDate = createdAt.format(DateTimeFormatter.ofPattern("YYYY-MM-dd hh:mm"));
        return String.format(
            "Добавлен ответ на вопрос:\nСсылка: %s\nДата: %s\n",
            url,
            formattedDate
        );
    }
}
