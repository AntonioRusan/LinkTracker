package edu.java.data_fetchers.models.github;

import java.time.OffsetDateTime;

public record RepositoryUpdate(
    String name,
    String htmlUrl,
    OffsetDateTime updatedAt
) {
    public String toString() {
        return String.format(
            "Обновление репозитория: %s;\nДата: %s\nСсылка: %s;\n",
            name,
            htmlUrl,
            updatedAt
        );
    }
}
