/*
 * This file is generated by jOOQ.
 */
package edu.java.domain.jooq.tables.records;


import edu.java.domain.jooq.tables.StackoverflowLink;

import java.beans.ConstructorProperties;
import java.time.OffsetDateTime;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Record1;
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
public class StackoverflowLinkRecord extends UpdatableRecordImpl<StackoverflowLinkRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>STACKOVERFLOW_LINK.LINK_ID</code>.
     */
    public void setLinkId(@NotNull Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>STACKOVERFLOW_LINK.LINK_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getLinkId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>STACKOVERFLOW_LINK.LAST_ANSWER_DATE</code>.
     */
    public void setLastAnswerDate(@Nullable OffsetDateTime value) {
        set(1, value);
    }

    /**
     * Getter for <code>STACKOVERFLOW_LINK.LAST_ANSWER_DATE</code>.
     */
    @Nullable
    public OffsetDateTime getLastAnswerDate() {
        return (OffsetDateTime) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached StackoverflowLinkRecord
     */
    public StackoverflowLinkRecord() {
        super(StackoverflowLink.STACKOVERFLOW_LINK);
    }

    /**
     * Create a detached, initialised StackoverflowLinkRecord
     */
    @ConstructorProperties({ "linkId", "lastAnswerDate" })
    public StackoverflowLinkRecord(@NotNull Long linkId, @Nullable OffsetDateTime lastAnswerDate) {
        super(StackoverflowLink.STACKOVERFLOW_LINK);

        setLinkId(linkId);
        setLastAnswerDate(lastAnswerDate);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised StackoverflowLinkRecord
     */
    public StackoverflowLinkRecord(edu.java.domain.jooq.tables.pojos.StackoverflowLink value) {
        super(StackoverflowLink.STACKOVERFLOW_LINK);

        if (value != null) {
            setLinkId(value.getLinkId());
            setLastAnswerDate(value.getLastAnswerDate());
            resetChangedOnNotNull();
        }
    }
}
