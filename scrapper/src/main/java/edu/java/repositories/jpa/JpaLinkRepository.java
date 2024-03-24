package edu.java.repositories.jpa;

import edu.java.models.Link;
import edu.java.models.jpa.LinkEntity;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLinkRepository extends JpaRepository<LinkEntity, Long> {
    @Query(value = "SELECT l FROM LinkEntity l WHERE l.url = ?1")
    Optional<LinkEntity> findByUrl(String url);

    @Query(value = "SELECT l FROM LinkEntity l WHERE l.lastCheckTime < ?1")
    List<LinkEntity> findOlderThanIntervalLinks(Duration interval);
}
