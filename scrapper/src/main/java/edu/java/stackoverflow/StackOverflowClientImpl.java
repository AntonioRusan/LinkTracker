package edu.java.stackoverflow;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class StackOverflowClientImpl implements StackOverflowClient {

    private final WebClient webClient;
    private final static Logger LOGGER = LogManager.getLogger();

    @Autowired
    public StackOverflowClientImpl(@Qualifier("stackOverflowWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public QuestionResponse getQuestionResponse(Long questionId) {
        return webClient
            .get()
            .uri("/questions/{questionId}?site=stackoverflow", questionId)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(QuestionResponse.class)
            .doOnError(error -> LOGGER.error("An error has occurred {}", error.getMessage()))
            .block();
    }

    @Override
    public QuestionResponse getQuestion(String url) {
        try {
            Long questionId = getQuestionIdFromUrl(url);
            return getQuestionResponse(questionId);
        } catch (Exception ex) {
            LOGGER.error("Не удалось отследить ссылку: " + ex.getMessage());
            return null;
        }
    }

    public Long getQuestionIdFromUrl(String inputUrl) {
        try {
            URL url = new URI(inputUrl).toURL();
            String path = url.getPath();
            Pattern patternPath = Pattern.compile("^/questions/(\\d+)/");
            Matcher matcherPath = patternPath.matcher(path);
            if (matcherPath.find()) {
                return Long.parseLong(matcherPath.group(1));
            } else {
                throw new MalformedURLException();
            }
        } catch (MalformedURLException | URISyntaxException ex) {
            throw new RuntimeException("Неправильная ссылка на StackOverflow");
        }
    }
}
