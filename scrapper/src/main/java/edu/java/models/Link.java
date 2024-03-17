package edu.java.models;

import java.time.OffsetDateTime;


public record Link(
        Long id,
        String url,
        OffsetDateTime lastCheckTime,
        OffsetDateTime updatedAt
) {
    public static Link create(String url) {
        return new Link(0L, url, OffsetDateTime.now(), OffsetDateTime.now());
    }
}
