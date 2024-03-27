package edu.java.clients.github.models.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.OffsetDateTime;

public record EventsResponse(
    String type,
    @JsonProperty("created_at")
    OffsetDateTime createdAt,
    JsonNode payload
) {

}
