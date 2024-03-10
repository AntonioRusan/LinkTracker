package edu.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.messageProcessors.BotMessageProcessor;
import edu.java.bot.models.User;
import edu.java.bot.repositories.UserRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

public class BotCommandsTest {
    private final String userFirstName = "John";
    private final String userLastName = "Snow";
    private final Long chatId = 1L;
    private final User testUser = new User(chatId, userFirstName, userLastName);

    private Update getFakeUpdate(String fakeMessageText) {

        Update fakeUpdate = Mockito.mock(Update.class);
        Message fakeMessage = Mockito.mock(Message.class);
        Chat fakeChat = Mockito.mock(Chat.class);

        when(fakeChat.id()).thenReturn(chatId);
        when(fakeChat.firstName()).thenReturn(userFirstName);
        when(fakeChat.lastName()).thenReturn(userLastName);

        when(fakeMessage.chat()).thenReturn(fakeChat);
        when(fakeMessage.text()).thenReturn(fakeMessageText);

        when(fakeUpdate.message()).thenReturn(fakeMessage);
        return fakeUpdate;
    }

    @Test
    @DisplayName("Проверка команды регистрации /start")
    public void testStartCommand() {

        Update fakeUpdate = getFakeUpdate("/start");

        UserRepository userRepository = new UserRepository();

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor(userRepository);

        SendMessage expectedResponse = new SendMessage(chatId, "Привет, " + userFirstName
            + " " + userLastName + "!\nВы зарегистрированы!");

        SendMessage actualResponse = botMessageProcessor.processMessage(fakeUpdate);

        assertThat(expectedResponse.getParameters()).isEqualTo(actualResponse.getParameters());
    }

    @Test
    @DisplayName("Проверка команды /list - нет отслеживаемых ссылок")
    public void testEmptyListCommand() {
        Update fakeUpdate = getFakeUpdate("/list");

        UserRepository userRepository = new UserRepository();

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor(userRepository);

        SendMessage expectedResponse = new SendMessage(chatId, "Нет отслеживаемых ссылок!");

        SendMessage actualResponse = botMessageProcessor.processMessage(fakeUpdate);

        assertThat(expectedResponse.getParameters()).isEqualTo(actualResponse.getParameters());
    }

    @Test
    @DisplayName("Проверка команды /list - нет отслеживаемых ссылок")
    public void testListCommand() {
        List<String> trackedUrls = List.of("https://stackoverflow.com/", "https://github.com/");

        Update fakeUpdate = getFakeUpdate("/list");

        UserRepository userRepository = new UserRepository();
        userRepository.addUser(chatId, testUser);
        for (String url : trackedUrls) {
            userRepository.addLinkToUser(chatId, url);
        }

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor(userRepository);

        SendMessage expectedResponse =
            new SendMessage(chatId, "Отслеживаемые ссылки:\n" + String.join("\n", trackedUrls));

        SendMessage actualResponse = botMessageProcessor.processMessage(fakeUpdate);

        assertThat(expectedResponse.getParameters()).isEqualTo(actualResponse.getParameters());
    }

    @Test
    @DisplayName("Проверка команды /help")
    public void testHelpCommand() {
        Update fakeUpdate = getFakeUpdate("/help");

        UserRepository userRepository = new UserRepository();

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor(userRepository);

        SendMessage expectedResponse =
            new SendMessage(chatId, "Список команд:\n" + String.join("\n", botMessageProcessor.commandDescriptionList));

        SendMessage actualResponse = botMessageProcessor.processMessage(fakeUpdate);

        assertThat(expectedResponse.getParameters()).isEqualTo(actualResponse.getParameters());
    }

    @Test
    @DisplayName("Проверка команды /track - валидная ссылка")
    public void testTrackCommand() {
        String url = "https://stackoverflow.com/";

        Update fakeUpdate = getFakeUpdate("/track " + url);

        UserRepository userRepository = new UserRepository();
        userRepository.addUser(chatId, testUser);

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor(userRepository);

        SendMessage expectedResponse = new SendMessage(chatId, "Отслеживаем изменения по ссылке: " + url);

        SendMessage actualResponse = botMessageProcessor.processMessage(fakeUpdate);

        assertThat(expectedResponse.getParameters()).isEqualTo(actualResponse.getParameters());
    }

    @Test
    @DisplayName("Проверка команды /track - невалидная ссылка")
    public void testTrackCommandNotValidUrl() {
        String url = "not.a_url";

        Update fakeUpdate = getFakeUpdate("/track " + url);

        UserRepository userRepository = new UserRepository();
        userRepository.addUser(chatId, testUser);

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor(userRepository);

        SendMessage expectedResponse = new SendMessage(chatId, "Введена неверная или пустая ссылка!");

        SendMessage actualResponse = botMessageProcessor.processMessage(fakeUpdate);

        assertThat(expectedResponse.getParameters()).isEqualTo(actualResponse.getParameters());
    }

    @Test
    @DisplayName("Проверка команды /untrack - валидная ссылка")
    public void testUntrackCommand() {
        String url = "https://stackoverflow.com/";

        Update fakeUpdate = getFakeUpdate("/untrack " + url);

        UserRepository userRepository = new UserRepository();
        userRepository.addUser(chatId, testUser);
        userRepository.addLinkToUser(chatId, url);

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor(userRepository);

        SendMessage expectedResponse = new SendMessage(chatId, "Прекращаем отслеживание изменений по ссылке: "
            + url);

        SendMessage actualResponse = botMessageProcessor.processMessage(fakeUpdate);

        assertThat(expectedResponse.getParameters()).isEqualTo(actualResponse.getParameters());
    }

    @Test
    @DisplayName("Проверка команды /untrack - невалидная ссылка")
    public void testUntrackCommandNotValidUrl() {
        String url = "not.a_url";

        Update fakeUpdate = getFakeUpdate("/untrack " + url);

        UserRepository userRepository = new UserRepository();
        userRepository.addUser(chatId, testUser);

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor(userRepository);

        SendMessage expectedResponse = new SendMessage(chatId, "Введена неверная или пустая ссылка!");

        SendMessage actualResponse = botMessageProcessor.processMessage(fakeUpdate);

        assertThat(expectedResponse.getParameters()).isEqualTo(actualResponse.getParameters());
    }
}
