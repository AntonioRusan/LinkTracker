package edu.java.repositories.jooq;

import edu.java.models.Link;
import edu.java.repositories.LinkRepositoryInterface;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.tables.Link.LINK;
import static org.jooq.impl.DSL.asterisk;

@Repository
public class JooqLinkRepository implements LinkRepositoryInterface {
    private final DSLContext dsl;

    public JooqLinkRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Optional<Link> findById(Long id) {
        return dsl.selectFrom(LINK)
            .where(LINK.ID.equal(id))
            .fetchOptionalInto(Link.class);
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        return dsl.selectFrom(LINK)
            .where(LINK.URL.equal(url))
            .fetchOptionalInto(Link.class);
    }

    @Override
    public List<Link> findAll() {
        return dsl.select(asterisk()).from(LINK).fetch().into(Link.class);
    }

    @Override
    public Long add(Link chat) {
        return dsl.insertInto(LINK)
            .set(LINK.URL, chat.url())
            .set(LINK.LAST_CHECK_TIME, chat.lastCheckTime())
            .set(LINK.UPDATED_AT, chat.updatedAt())
            .returningResult(LINK.ID)
            .fetchOneInto(Long.class);
    }

    @Override
    public Integer delete(Long id) {
        return dsl.delete(LINK)
            .where(LINK.ID.equal(id))
            .execute();
    }

    @Override
    public List<Link> findOlderThanIntervalLinks(Duration interval) {
        OffsetDateTime minTime = OffsetDateTime.now().minus(interval);
        return dsl.select(asterisk()).from(LINK).where(LINK.LAST_CHECK_TIME.lessThan(minTime)).fetch().into(Link.class);
    }

    @Override
    public void updateLastCheckAndUpdatedTime(Long id, OffsetDateTime lastCheckTime, OffsetDateTime updatedAt) {
        dsl.update(LINK)
            .set(LINK.LAST_CHECK_TIME, lastCheckTime)
            .set(LINK.UPDATED_AT, updatedAt)
            .where(LINK.ID.equal(id))
            .execute();
    }

    @Override
    public void updateLastCheckTime(Long id, OffsetDateTime lastCheckTime) {
        dsl.update(LINK)
            .set(LINK.LAST_CHECK_TIME, lastCheckTime)
            .where(LINK.ID.equal(id))
            .execute();
    }
}
