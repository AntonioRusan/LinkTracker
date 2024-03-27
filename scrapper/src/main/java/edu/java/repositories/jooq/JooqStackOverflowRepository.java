package edu.java.repositories.jooq;

import edu.java.models.StackOverflowLink;
import edu.java.repositories.StackOverflowLinkInterface;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.tables.StackoverflowLink.STACKOVERFLOW_LINK;

@Repository
public class JooqStackOverflowRepository implements StackOverflowLinkInterface {
    private final DSLContext dsl;

    public JooqStackOverflowRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Optional<StackOverflowLink> findByLinkId(Long linkId) {
        return dsl.selectFrom(STACKOVERFLOW_LINK)
            .where(STACKOVERFLOW_LINK.LINK_ID.equal(linkId))
            .fetchOptionalInto(StackOverflowLink.class);
    }

    @Override
    public Integer add(StackOverflowLink link) {
        return dsl.insertInto(STACKOVERFLOW_LINK)
            .set(STACKOVERFLOW_LINK.LINK_ID, link.linkId())
            .set(STACKOVERFLOW_LINK.LAST_ANSWER_DATE, link.lastAnswerDate())
            .execute();
    }

    @Override
    public void updateLastAnswerDate(Long linkId, OffsetDateTime lastAnswerDate) {
        dsl.update(STACKOVERFLOW_LINK)
            .set(STACKOVERFLOW_LINK.LAST_ANSWER_DATE, lastAnswerDate)
            .where(STACKOVERFLOW_LINK.LINK_ID.equal(linkId))
            .execute();
    }
}
