package edu.java.clients.stackoverflow.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record AnswersResponse(
    List<AnswersResponse.ItemResponse> items
) {
    public record ItemResponse(
        @JsonProperty("answer_id")
        Long answerId,
        @JsonProperty("question_id")
        Long questionId,
        @JsonProperty("creation_date")
        OffsetDateTime creationDate
    ) {

    }
}
