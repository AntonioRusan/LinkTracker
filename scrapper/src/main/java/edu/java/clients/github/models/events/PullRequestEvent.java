package edu.java.clients.github.models.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PullRequestEvent(
    @JsonProperty("html_url")
    String htmlUrl,
    String title
) {
}
