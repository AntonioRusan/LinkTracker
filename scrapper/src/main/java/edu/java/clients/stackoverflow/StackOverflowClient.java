package edu.java.clients.stackoverflow;

import edu.java.clients.stackoverflow.models.AnswersResponse;
import edu.java.clients.stackoverflow.models.QuestionResponse;
import java.net.URI;

public interface StackOverflowClient {
    QuestionResponse getQuestion(URI url);

    AnswersResponse getAnswers(URI url);
}
