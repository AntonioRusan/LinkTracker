package edu.java.clients.stackoverflow;

import edu.java.clients.stackoverflow.models.QuestionResponse;

public interface StackOverflowClient {
    QuestionResponse getQuestionResponse(Long questionId);

    QuestionResponse getQuestion(String url);
}
