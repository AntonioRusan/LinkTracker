package edu.java.stackoverflow;

public interface StackOverflowClient {
    QuestionResponse getQuestionResponse(Long questionId);

    QuestionResponse getQuestion(String url);
}
