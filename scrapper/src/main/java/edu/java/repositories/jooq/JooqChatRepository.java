package edu.java.repositories.jooq;

import edu.java.models.Chat;
import edu.java.repositories.ChatRepositoryInterface;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.Tables.CHAT;
import static org.jooq.impl.DSL.asterisk;

@Repository
public class JooqChatRepository implements ChatRepositoryInterface {
    private final DSLContext dsl;

    public JooqChatRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Optional<Chat> findById(Long id) {
        return dsl.selectFrom(CHAT)
            .where(CHAT.ID.equal(id))
            .fetchOptionalInto(Chat.class);
    }

    @Override
    public List<Chat> findAll() {
        return dsl.select(asterisk()).from(CHAT).fetch().into(Chat.class);
    }

    @Override
    public Integer add(Chat chat) {
        return dsl.insertInto(CHAT)
            .set(CHAT.ID, chat.id())
            .execute();
    }

    @Override
    public Integer delete(Long id) {
        return dsl.delete(CHAT)
            .where(CHAT.ID.equal(id))
            .execute();
    }
}
