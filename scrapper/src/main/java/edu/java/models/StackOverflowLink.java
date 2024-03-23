package edu.java.models;

import java.time.OffsetDateTime;

public record StackOverflowLink(
    Long linkId,
    OffsetDateTime lastAnswerDate
) {
}
