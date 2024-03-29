/*
 * This file is generated by jOOQ.
 */
package edu.java.domain.jooq;


import edu.java.domain.jooq.tables.*;
import edu.java.domain.jooq.tables.records.*;
import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;

import javax.annotation.processing.Generated;


/**
 * A class modelling foreign key relationships and constraints of tables in the
 * default schema.
 */
@Generated(
        value = {
                "https://www.jooq.org",
                "jOOQ version:3.19.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ChatRecord> CONSTRAINT_1 = Internal.createUniqueKey(Chat.CHAT, DSL.name("CONSTRAINT_1"), new TableField[]{Chat.CHAT.ID}, true);
    public static final UniqueKey<ChatLinkRecord> CONSTRAINT_868 = Internal.createUniqueKey(ChatLink.CHAT_LINK, DSL.name("CONSTRAINT_868"), new TableField[]{ChatLink.CHAT_LINK.CHAT_ID, ChatLink.CHAT_LINK.LINK_ID}, true);
    public static final UniqueKey<GithubLinkRecord> CONSTRAINT_D8 = Internal.createUniqueKey(GithubLink.GITHUB_LINK, DSL.name("CONSTRAINT_D8"), new TableField[]{GithubLink.GITHUB_LINK.LINK_ID}, true);
    public static final UniqueKey<LinkRecord> CONSTRAINT_2 = Internal.createUniqueKey(Link.LINK, DSL.name("CONSTRAINT_2"), new TableField[]{Link.LINK.ID}, true);
    public static final UniqueKey<LinkRecord> CONSTRAINT_23 = Internal.createUniqueKey(Link.LINK, DSL.name("CONSTRAINT_23"), new TableField[]{Link.LINK.URL}, true);
    public static final UniqueKey<StackoverflowLinkRecord> CONSTRAINT_93 = Internal.createUniqueKey(StackoverflowLink.STACKOVERFLOW_LINK, DSL.name("CONSTRAINT_93"), new TableField[]{StackoverflowLink.STACKOVERFLOW_LINK.LINK_ID}, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<ChatLinkRecord, ChatRecord> CONSTRAINT_8 = Internal.createForeignKey(ChatLink.CHAT_LINK, DSL.name("CONSTRAINT_8"), new TableField[]{ChatLink.CHAT_LINK.CHAT_ID}, Keys.CONSTRAINT_1, new TableField[]{Chat.CHAT.ID}, true);
    public static final ForeignKey<ChatLinkRecord, LinkRecord> CONSTRAINT_86 = Internal.createForeignKey(ChatLink.CHAT_LINK, DSL.name("CONSTRAINT_86"), new TableField[]{ChatLink.CHAT_LINK.LINK_ID}, Keys.CONSTRAINT_2, new TableField[]{Link.LINK.ID}, true);
    public static final ForeignKey<GithubLinkRecord, LinkRecord> CONSTRAINT_D = Internal.createForeignKey(GithubLink.GITHUB_LINK, DSL.name("CONSTRAINT_D"), new TableField[]{GithubLink.GITHUB_LINK.LINK_ID}, Keys.CONSTRAINT_2, new TableField[]{Link.LINK.ID}, true);
    public static final ForeignKey<StackoverflowLinkRecord, LinkRecord> CONSTRAINT_9 = Internal.createForeignKey(StackoverflowLink.STACKOVERFLOW_LINK, DSL.name("CONSTRAINT_9"), new TableField[]{StackoverflowLink.STACKOVERFLOW_LINK.LINK_ID}, Keys.CONSTRAINT_2, new TableField[]{Link.LINK.ID}, true);
}
