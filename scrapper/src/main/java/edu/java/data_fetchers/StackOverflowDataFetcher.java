package edu.java.data_fetchers;

import edu.java.clients.stackoverflow.StackOverflowClient;
import edu.java.clients.stackoverflow.models.QuestionResponse;
import edu.java.data_fetchers.models.stackoverflow.QuestionUpdate;
import edu.java.models.Link;
import edu.java.services.links.LinksService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class StackOverflowDataFetcher {
    private final StackOverflowClient stackOverflowClient;
    private final LinksService linksService;

    private final static Logger LOGGER = LogManager.getLogger();

    public StackOverflowDataFetcher(StackOverflowClient stackOverflowClient, LinksService linksService) {
        this.stackOverflowClient = stackOverflowClient;
        this.linksService = linksService;
    }

    public String fetchData(Link link) {
        URI uri = URI.create(link.url());
        Optional<QuestionUpdate> fetchedQuestion = fetchQuestionData(link);
        List<String> description = new ArrayList<>();
        fetchedQuestion.ifPresent(repository -> description.add(repository.toString()));
        linksService.updateLinkCheckTime(link.id(), OffsetDateTime.now());
        return String.join("\n\n", description);
    }

    public Optional<QuestionUpdate> fetchQuestionData(Link link) {
        URI uri = URI.create(link.url());
        QuestionResponse questionResponse = stackOverflowClient.getQuestion(uri);
        if (!questionResponse.items().isEmpty()) {
            QuestionResponse.ItemResponse question = questionResponse.items().getFirst();
            if (question.lastActivityDate().isAfter(link.updatedAt())) {
                linksService.updateLinkCheckAndUpdatedTime(
                    link.id(),
                    OffsetDateTime.now(),
                    question.lastActivityDate()
                );
                return Optional.of(new QuestionUpdate(
                    question.title(),
                    question.link(),
                    question.lastActivityDate()
                ));
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
}
