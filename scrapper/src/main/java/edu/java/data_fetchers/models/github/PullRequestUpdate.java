package edu.java.data_fetchers.models.github;

import java.time.OffsetDateTime;

public record PullRequestUpdate(
    String title,
    OffsetDateTime createdAt,
    String htmlUrl
) {
    public String toString() {
        return String.format(
            "Добавлен новый Pull Request: %s;\nДата: %s;\nСсылка: %s;\n",
            title,
            createdAt,
            htmlUrl
        );
    }
}
