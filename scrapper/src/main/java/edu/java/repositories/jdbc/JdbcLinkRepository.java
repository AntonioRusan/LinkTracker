package edu.java.repositories.jdbc;

import edu.java.models.Link;
import edu.java.repositories.LinkRepositoryInterface;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("MultipleStringLiterals")
public class JdbcLinkRepository implements LinkRepositoryInterface {
    private final JdbcClient jdbcClient;

    public JdbcLinkRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<Link> findById(Long id) {
        return jdbcClient.sql("SELECT * FROM link WHERE id = :id")
            .param("id", id)
            .query(Link.class)
            .optional();
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        return jdbcClient.sql("SELECT * FROM link WHERE url = :url")
            .param("url", url)
            .query(Link.class)
            .optional();
    }

    @Override
    public List<Link> findAll() {
        return jdbcClient.sql("SELECT * FROM link")
            .query(Link.class)
            .list();
    }

    @Override
    public Long add(Link link) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(
                "INSERT INTO link(url, last_check_time, updated_at) VALUES (:url, :lastCheckTime, :updatedAt)"
            )
            .param("url", link.url())
            .param("lastCheckTime", link.lastCheckTime())
            .param("updatedAt", link.updatedAt())
            .update(keyHolder, "id");
        return (Long) keyHolder.getKey();
    }

    @Override
    public Integer delete(Long id) {
        return jdbcClient.sql("DELETE FROM link WHERE id = :id")
            .param("id", id)
            .update();
    }

    @Override
    public List<Link> findOlderThanIntervalLinks(Duration interval) {
        return jdbcClient.sql("SELECT * FROM link WHERE link.last_check_time < :min_time")
            .param("min_time", OffsetDateTime.now().minus(interval))
            .query(Link.class)
            .list();
    }

    @Override
    public void updateLastCheckAndUpdatedTime(Long id, OffsetDateTime lastCheckTime, OffsetDateTime updatedAt) {
        jdbcClient.sql("UPDATE link SET last_check_time = :lastCheckTime, updated_at = :updatedAt WHERE id = :id")
            .param("id", id)
            .param("lastCheckTime", lastCheckTime)
            .param("updatedAt", updatedAt)
            .update();
    }

    @Override
    public void updateLastCheckTime(Long id, OffsetDateTime lastCheckTime) {
        jdbcClient.sql("UPDATE link SET last_check_time = :lastCheckTime WHERE id = :id")
            .param("id", id)
            .param("lastCheckTime", lastCheckTime)
            .update();
    }
}
