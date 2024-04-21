package edu.java.configuration.database;

import edu.java.repositories.jooq.JooqChatLinkRepository;
import edu.java.repositories.jooq.JooqChatRepository;
import edu.java.repositories.jooq.JooqGitHubLinkRepository;
import edu.java.repositories.jooq.JooqLinkRepository;
import edu.java.repositories.jooq.JooqStackOverflowLinkRepository;
import edu.java.services.links.JooqLinksServiceImpl;
import edu.java.services.links.LinksService;
import edu.java.services.tgChat.JooqTgChatServiceImpl;
import edu.java.services.tgChat.TgChatService;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqDatabaseConfiguration {
    @Bean
    public DefaultConfigurationCustomizer postgresJooqCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
            .withRenderSchema(false)
            .withRenderFormatted(true)
            .withRenderQuotedNames(RenderQuotedNames.NEVER);
    }

    @Bean
    public TgChatService tgChatService(
        JooqChatRepository chatRepository,
        JooqChatLinkRepository chatLinkRepository
    ) {
        return new JooqTgChatServiceImpl(chatRepository, chatLinkRepository);
    }

    @Bean
    public LinksService linksService(
        JooqLinkRepository linkRepository,
        JooqChatRepository chatRepository,
        JooqChatLinkRepository chatLinkRepository,
        JooqGitHubLinkRepository gitHubLinkRepository,
        JooqStackOverflowLinkRepository stackOverflowLinkRepository
    ) {
        return new JooqLinksServiceImpl(
            linkRepository,
            chatRepository,
            chatLinkRepository,
            gitHubLinkRepository,
            stackOverflowLinkRepository
        );
    }

}
