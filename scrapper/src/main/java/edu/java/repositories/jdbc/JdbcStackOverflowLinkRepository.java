package edu.java.repositories.jdbc;

import edu.java.models.StackOverflowLink;
import edu.java.repositories.StackOverflowLinkInterface;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("MultipleStringLiterals")
public class JdbcStackOverflowLinkRepository implements StackOverflowLinkInterface {
    private final JdbcClient jdbcClient;

    public JdbcStackOverflowLinkRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<StackOverflowLink> findByLinkId(Long linkId) {
        return jdbcClient.sql("SELECT * FROM stackoverflow_link WHERE link_id = :linkId")
            .param("linkId", linkId)
            .query(StackOverflowLink.class)
            .optional();
    }

    @Override
    public Integer add(StackOverflowLink link) {
        return jdbcClient.sql(
                "INSERT INTO stackoverflow_link(link_id, last_answer_date) VALUES (:linkId, :lastAnswerDate)"
            )
            .param("linkId", link.linkId())
            .param("lastAnswerDate", link.lastAnswerDate())
            .update();
    }

    @Override
    public void updateLastAnswerDate(Long linkId, OffsetDateTime lastAnswerDate) {
        jdbcClient.sql(
                "UPDATE stackoverflow_link SET last_answer_date = :lastAnswerDate WHERE link_id = :linkId")
            .param("linkId", linkId)
            .param("lastAnswerDate", lastAnswerDate)
            .update();
    }
}
