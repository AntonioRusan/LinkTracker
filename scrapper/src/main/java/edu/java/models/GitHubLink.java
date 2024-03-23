package edu.java.models;

import java.time.OffsetDateTime;

public record GitHubLink(
    Long linkId,
    OffsetDateTime lastPullRequestDate
) {
}
