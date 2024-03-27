package edu.java.clients.github.models.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Payload(
    @JsonProperty("pull_request")
    PullRequestEvent pullRequestEvent
) {
}
