package edu.java;

import java.net.URI;
import java.time.OffsetDateTime;

public record FetchedLinkData(
    URI url,
    OffsetDateTime lastUpdatedAt,
    Long resourceId,
    String description
) {
}
