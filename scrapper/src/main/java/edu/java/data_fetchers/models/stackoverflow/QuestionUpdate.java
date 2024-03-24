package edu.java.data_fetchers.models.stackoverflow;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public record QuestionUpdate(
    String name,
    String htmlUrl,
    OffsetDateTime updatedAt
) {

    public String toString() {
        String formattedDate = updatedAt.format(DateTimeFormatter.ofPattern("YYYY-MM-dd hh:mm"));
        return String.format(
            "Обновление вопроса:\n%s\nСсылка: %s\nДата: %s\n",
            name,
            htmlUrl,
            formattedDate
        );
    }
}
