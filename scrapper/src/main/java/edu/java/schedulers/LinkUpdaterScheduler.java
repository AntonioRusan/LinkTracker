package edu.java.schedulers;

import api.bot.models.LinkUpdate;
import edu.java.clients.bot.BotClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.data_fetchers.DataFetcher;
import edu.java.models.Chat;
import edu.java.services.links.LinksService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final static Logger LOGGER = LogManager.getLogger();
    private final ApplicationConfig applicationConfig;
    private final LinksService linksService;
    private final DataFetcher dataFetcher;
    private final BotClient botClient;

    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    public void update() {
        try {
            LOGGER.info("Периодическая проверка обновлений по ссылкам из базы данных");
            linksService.findOlderThanIntervalLinks(applicationConfig.scheduler().forceCheckDelay()).forEach(
                link -> {
                    LOGGER.info("Проверка ссылки: {}", link.url());
                    URI uri = URI.create(link.url());

                    String fetchedLinkData = dataFetcher.fetchData(link);
                    if (!fetchedLinkData.isEmpty()) {
                        LOGGER.info(
                            "Cсылка обновилась с id: {}, прошлое обновление {}",
                            link.id(),
                            link.updatedAt()
                        );
                        List<Long> tgChatIds =
                            linksService.findAllChatByLinkId(link.id()).stream().map(Chat::id).toList();
                        botClient.sendUpdate(new LinkUpdate(
                            link.id(),
                            uri,
                            fetchedLinkData,
                            tgChatIds
                        ));
                    } else {
                        LOGGER.info(
                            "Cсылка не обновилась, последнее обновление {}",
                            link.updatedAt()
                        );
                    }
                }
            );
        } catch (Exception ex) {
            LOGGER.info("Ошибка во время периодического обновления данных: {}", ex.getMessage());
        }

    }
}
