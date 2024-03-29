/*
 * This file is generated by jOOQ.
 */
package edu.java.domain.jooq.tables.records;


import edu.java.domain.jooq.tables.ChatLink;

import java.beans.ConstructorProperties;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jooq.Record2;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.19.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class ChatLinkRecord extends UpdatableRecordImpl<ChatLinkRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>CHAT_LINK.CHAT_ID</code>.
     */
    public void setChatId(@NotNull Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>CHAT_LINK.CHAT_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getChatId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>CHAT_LINK.LINK_ID</code>.
     */
    public void setLinkId(@NotNull Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>CHAT_LINK.LINK_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getLinkId() {
        return (Long) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record2<Long, Long> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ChatLinkRecord
     */
    public ChatLinkRecord() {
        super(ChatLink.CHAT_LINK);
    }

    /**
     * Create a detached, initialised ChatLinkRecord
     */
    @ConstructorProperties({ "chatId", "linkId" })
    public ChatLinkRecord(@NotNull Long chatId, @NotNull Long linkId) {
        super(ChatLink.CHAT_LINK);

        setChatId(chatId);
        setLinkId(linkId);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised ChatLinkRecord
     */
    public ChatLinkRecord(edu.java.domain.jooq.tables.pojos.ChatLink value) {
        super(ChatLink.CHAT_LINK);

        if (value != null) {
            setChatId(value.getChatId());
            setLinkId(value.getLinkId());
            resetChangedOnNotNull();
        }
    }
}
