package edu.java.data_fetchers.models.github;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public record RepositoryUpdate(
    String name,
    String htmlUrl,
    OffsetDateTime updatedAt
) {
    public String toString() {
        String formattedDate = updatedAt.format(DateTimeFormatter.ofPattern("YYYY-MM-dd hh:mm"));
        return String.format(
            "Обновление репозитория: %s;\nДата: %s\nСсылка: %s;\n",
            name,
            htmlUrl,
            formattedDate
        );
    }
}
