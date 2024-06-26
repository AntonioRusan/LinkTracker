package edu.java.data_fetchers;

import edu.java.clients.stackoverflow.StackOverflowClient;
import edu.java.clients.stackoverflow.models.AnswersResponse;
import edu.java.clients.stackoverflow.models.QuestionResponse;
import edu.java.data_fetchers.models.UpdateEvent;
import edu.java.data_fetchers.models.UpdateEventType;
import edu.java.models.Link;
import edu.java.services.links.LinksService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
        Optional<UpdateEvent> fetchedQuestion = fetchQuestionData(link);
        List<UpdateEvent> fetchedAnswers = fetchAnswersData(link);
        List<String> description = new ArrayList<>();
        fetchedQuestion.ifPresent(repository -> description.add(repository.toString()));
        if (!fetchedAnswers.isEmpty()) {
            List<String> answersDescription =
                fetchedAnswers.stream().map(UpdateEvent::toString).toList();
            description.add("Обновление ответов:\n\n" + String.join("\n", answersDescription));
        }
        linksService.updateLinkCheckTime(link.id(), OffsetDateTime.now());
        return String.join("\n\n", description);
    }

    public Optional<UpdateEvent> fetchQuestionData(Link link) {
        URI uri = URI.create(link.url());
        QuestionResponse questionResponse = stackOverflowClient.getQuestion(uri);
        if (questionResponse != null && !questionResponse.items().isEmpty()) {
            QuestionResponse.ItemResponse question = questionResponse.items().getFirst();
            if (question.lastActivityDate().isAfter(link.updatedAt())) {
                linksService.updateLinkCheckAndUpdatedTime(
                    link.id(),
                    OffsetDateTime.now(),
                    question.lastActivityDate()
                );
                return Optional.of(new UpdateEvent(
                    UpdateEventType.QUESTION_UPDATE,
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

    public List<UpdateEvent> fetchAnswersData(Link link) {
        URI uri = URI.create(link.url());
        AnswersResponse answersResponse = stackOverflowClient.getAnswers(uri);
        List<UpdateEvent> answers = new ArrayList<>();

        linksService.findStackOverflowByLinkId(link.id()).ifPresent(stackOverflowLink -> {
            if (answersResponse != null && !answersResponse.items().isEmpty()) {
                answersResponse.items().forEach(
                    answer -> {
                        if (answer.creationDate().isAfter(stackOverflowLink.lastAnswerDate())) {
                            answers.add(new UpdateEvent(
                                UpdateEventType.NEW_ANSWER,
                                "",
                                String.format("https://stackoverflow.com/questions/%d/#%d",
                                    answer.questionId(), answer.answerId()
                                ),
                                answer.creationDate()
                            ));
                        }
                    }
                );
                answers.stream().max(Comparator.comparing(UpdateEvent::createdAt)).ifPresent(
                    lastAnswer -> linksService.updateStackOverflowLastAnswerDate(
                        stackOverflowLink.linkId(),
                        lastAnswer.createdAt()
                    )
                );
            }
        });
        return answers;
    }
}
