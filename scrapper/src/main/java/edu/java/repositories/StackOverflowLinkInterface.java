package edu.java.repositories;

import edu.java.models.StackOverflowLink;
import java.time.OffsetDateTime;
import java.util.Optional;

public interface StackOverflowLinkInterface {
    Optional<StackOverflowLink> findByLinkId(Long linkId);

    Integer add(StackOverflowLink link);

    void updateLastAnswerDate(Long linkId, OffsetDateTime lastAnswerDate);
}
