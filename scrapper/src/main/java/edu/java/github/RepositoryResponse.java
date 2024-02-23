package edu.java.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record RepositoryResponse(
    Long id,
    String name,
    @JsonProperty("html_url")
    String htmlUrl,
    @JsonProperty("updated_at")
    OffsetDateTime updatedAt
) {
}
