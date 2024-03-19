package edu.java.repositories.jooq;

import edu.java.models.Chat;
import edu.java.models.Link;
import edu.java.repositories.ChatLinkRepositoryInterface;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.Tables.CHAT;
import static edu.java.domain.jooq.Tables.CHAT_LINK;
import static edu.java.domain.jooq.Tables.LINK;
import static org.jooq.impl.DSL.select;

@Repository
public class JooqChatLinkRepository implements ChatLinkRepositoryInterface {
    private final DSLContext dsl;

    public JooqChatLinkRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public List<Link> findAllLinksByChatId(Long chatId) {
        return select()
            .from(CHAT_LINK
                .join(LINK)
                .on(CHAT_LINK.LINK_ID.eq(LINK.ID))
                .where(CHAT_LINK.CHAT_ID.equal(chatId)))
            .fetchInto(Link.class);
    }

    @Override
    public List<Chat> findAllChatsByLinkId(Long linkId) {
        return select()
            .from(CHAT_LINK
                .join(CHAT)
                .on(CHAT_LINK.CHAT_ID.eq(CHAT.ID))
                .where(CHAT_LINK.LINK_ID.equal(linkId)))
            .fetchInto(Chat.class);
    }

    @Override
    public Integer add(Long chatId, Long linkId) {
        return dsl.insertInto(CHAT_LINK)
            .set(CHAT_LINK.CHAT_ID, chatId)
            .set(CHAT_LINK.LINK_ID, linkId)
            .execute();
    }

    @Override
    public Integer delete(Long chatId, Long linkId) {
        return dsl.delete(CHAT_LINK)
            .where(CHAT_LINK.CHAT_ID.equal(chatId).and(CHAT_LINK.LINK_ID.equal(linkId)))
            .execute();
    }

    @Override
    public Integer deleteByChatId(Long chatId) {
        return dsl.delete(CHAT_LINK)
            .where(CHAT_LINK.CHAT_ID.equal(chatId))
            .execute();
    }
}
