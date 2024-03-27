package edu.java.configuration;

import edu.java.repositories.jdbc.JdbcChatLinkRepository;
import edu.java.repositories.jdbc.JdbcChatRepository;
import edu.java.repositories.jdbc.JdbcGitHubLinkRepository;
import edu.java.repositories.jdbc.JdbcLinkRepository;
import edu.java.repositories.jdbc.JdbcStackOverflowLinkRepository;
import edu.java.services.links.JdbcLinksServiceImpl;
import edu.java.services.links.LinksService;
import edu.java.services.tgChat.JdbcTgChatServiceImpl;
import edu.java.services.tgChat.TgChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcDatabaseConfiguration {

    @Bean
    public TgChatService tgChatService(
        JdbcChatRepository chatRepository,
        JdbcChatLinkRepository chatLinkRepository
    ) {
        return new JdbcTgChatServiceImpl(chatRepository, chatLinkRepository);
    }

    @Bean
    public LinksService linksService(
        JdbcLinkRepository linkRepository,
        JdbcChatRepository chatRepository,
        JdbcChatLinkRepository chatLinkRepository,
        JdbcGitHubLinkRepository gitHubLinkRepository,
        JdbcStackOverflowLinkRepository stackOverflowLinkRepository
    ) {
        return new JdbcLinksServiceImpl(
            linkRepository,
            chatRepository,
            chatLinkRepository,
            gitHubLinkRepository,
            stackOverflowLinkRepository
        );
    }

}
