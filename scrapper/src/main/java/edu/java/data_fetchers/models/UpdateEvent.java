package edu.java.data_fetchers.models;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("MagicNumber")
public record UpdateEvent(
    UpdateEventType type,
    String name,
    String htmlUrl,
    OffsetDateTime createdAt
) {
    public String toString() {
        String delimeter = "-".repeat(80);
        String formattedDate = createdAt.format(DateTimeFormatter.ofPattern("YYYY-MM-dd hh:mm"));
        return String.format(
            "%s:\n%s\nСсылка: %s\nДата: %s\n%s\n",
            type.getDescription(),
            name,
            htmlUrl,
            formattedDate,
            delimeter
        );
    }
}
