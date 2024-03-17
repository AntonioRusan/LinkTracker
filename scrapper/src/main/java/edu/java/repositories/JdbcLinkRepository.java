package edu.java.repositories;

import edu.java.models.Link;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
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
    public List<Link> findAll() {
        return jdbcClient.sql("SELECT * FROM link")
            .query(Link.class)
            .list();
    }

    @Override
    public Integer add(Link link) {
        return jdbcClient.sql(
                "INSERT INTO link(url, last_check_time, updated_at) VALUES (:url, :lastCheckTime, :updatedAt)"
            )
            .param("url", link.url())
            .param("lastCheckTime", link.lastCheckTime())
            .param("updatedAt", link.updatedAt())
            .update();
    }

    @Override
    public Integer delete(Long id) {
        return jdbcClient.sql("DELETE FROM link WHERE id = :id")
            .param("id", id)
            .update();
    }
}
