package edu.java.data_fetchers.models.github;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public record PullRequestUpdate(
    String title,
    OffsetDateTime createdAt,
    String htmlUrl
) {
    public String toString() {
        String formattedDate = createdAt.format(DateTimeFormatter.ofPattern("YYYY-MM-dd hh:mm"));
        return String.format(
            "Добавлен новый Pull Request:\n%s\nДата: %s\nСсылка: %s\n",
            title,
            formattedDate,
            htmlUrl
        );
    }
}
