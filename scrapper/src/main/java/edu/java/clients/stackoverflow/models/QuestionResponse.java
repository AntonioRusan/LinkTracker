package edu.java.clients.stackoverflow.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record QuestionResponse(
    List<ItemResponse> items
) {
    public record ItemResponse(
        @JsonProperty("question_id")
        Long questionId,
        String title,
        String link,
        @JsonProperty("last_activity_date")
        OffsetDateTime lastActivityDate
    ) {

    }
}

