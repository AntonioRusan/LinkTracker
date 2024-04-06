package edu.java.configuration.database;

import edu.java.repositories.jpa.JpaChatRepository;
import edu.java.repositories.jpa.JpaGitHubLinkRepository;
import edu.java.repositories.jpa.JpaLinkRepository;
import edu.java.repositories.jpa.JpaStackOverflowLinkRepository;
import edu.java.services.links.JpaLinksServiceImpl;
import edu.java.services.links.LinksService;
import edu.java.services.tgChat.JpaTgChatServiceImpl;
import edu.java.services.tgChat.TgChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaDatabaseConfiguration {
    @Bean
    public TgChatService tgChatService(
        JpaChatRepository chatRepository
    ) {
        return new JpaTgChatServiceImpl(chatRepository);
    }

    @Bean
    public LinksService linksService(
        JpaLinkRepository linkRepository,
        JpaChatRepository chatRepository,
        JpaGitHubLinkRepository gitHubLinkRepository,
        JpaStackOverflowLinkRepository stackOverflowLinkRepository
    ) {
        return new JpaLinksServiceImpl(
            linkRepository,
            chatRepository,
            gitHubLinkRepository,
            stackOverflowLinkRepository
        );
    }
}
