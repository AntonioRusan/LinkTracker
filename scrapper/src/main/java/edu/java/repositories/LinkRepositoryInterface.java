package edu.java.repositories;

import edu.java.models.Link;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepositoryInterface {
    Optional<Link> findById(Long id);

    Optional<Link> findByUrl(String url);

    List<Link> findAll();

    Long add(Link chat);

    Integer delete(Long id);

    List<Link> findOlderThanIntervalLinks(Duration interval);

    void updateLastCheckAndUpdatedTime(Long id, OffsetDateTime lastCheckTime, OffsetDateTime updatedAt);

    void updateLastCheckTime(Long id, OffsetDateTime lastCheckTime);
}
