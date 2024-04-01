package edu.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.message_processors.BotMessageProcessor;
import edu.java.bot.models.User;
import edu.java.bot.repositories.UserRepository;
import edu.java.bot.services.bot_command.BotCommandService;
import edu.java.bot.services.bot_command.BotCommandServiceImpl;
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
        BotCommandService botCommandService = Mockito.mock(BotCommandServiceImpl.class);
        when(botCommandService.registerChat(chatId)).thenReturn("Вы успешно зарегистрированы!");

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor(botCommandService);

        SendMessage expectedResponse = new SendMessage(chatId, "Привет, " + userFirstName
            + " " + userLastName + "!\n" + "Вы успешно зарегистрированы!");

        SendMessage actualResponse = botMessageProcessor.processMessage(fakeUpdate);

        assertThat(expectedResponse.getParameters()).isEqualTo(actualResponse.getParameters());
    }

    @Test
    @DisplayName("Проверка команды /list - нет отслеживаемых ссылок")
    public void testEmptyListCommand() {
        Update fakeUpdate = getFakeUpdate("/list");

        BotCommandService botCommandService = Mockito.mock(BotCommandServiceImpl.class);
        when(botCommandService.listLinks(chatId)).thenReturn("");

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor(
            botCommandService
        );

        SendMessage expectedResponse = new SendMessage(chatId, "Нет отслеживаемых ссылок!");

        SendMessage actualResponse = botMessageProcessor.processMessage(fakeUpdate);

        assertThat(expectedResponse.getParameters()).isEqualTo(actualResponse.getParameters());
    }

    @Test
    @DisplayName("Проверка команды /list - есть отслеживаемые ссылки")
    public void testListCommand() {
        List<String> trackedUrls = List.of("https://stackoverflow.com/", "https://github.com/");

        Update fakeUpdate = getFakeUpdate("/list");

        BotCommandService botCommandService = Mockito.mock(BotCommandServiceImpl.class);
        when(botCommandService.listLinks(chatId)).thenReturn(String.join(";\n", trackedUrls));

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor(
            botCommandService
        );

        SendMessage expectedResponse =
            new SendMessage(chatId, "Отслеживаемые ссылки:\n" + String.join(";\n", trackedUrls));

        SendMessage actualResponse = botMessageProcessor.processMessage(fakeUpdate);

        assertThat(expectedResponse.getParameters()).isEqualTo(actualResponse.getParameters());
    }

    @Test
    @DisplayName("Проверка команды /help")
    public void testHelpCommand() {
        Update fakeUpdate = getFakeUpdate("/help");

        BotCommandService botCommandService = Mockito.mock(BotCommandServiceImpl.class);

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor(
            botCommandService
        );

        SendMessage expectedResponse =
            new SendMessage(
                chatId,
                "Список команд:\n" + String.join("\n", botMessageProcessor.commandDescriptionList())
            );

        SendMessage actualResponse = botMessageProcessor.processMessage(fakeUpdate);

        assertThat(expectedResponse.getParameters()).isEqualTo(actualResponse.getParameters());
    }

    @Test
    @DisplayName("Проверка команды /track - валидная ссылка")
    public void testTrackCommand() {
        String url = "https://stackoverflow.com/";

        Update fakeUpdate = getFakeUpdate("/track " + url);

        BotCommandService botCommandService = Mockito.mock(BotCommandServiceImpl.class);
        when(botCommandService.addLink(chatId, "https://stackoverflow.com/")).thenReturn(
            "Отслеживаем изменения по ссылке: " + url);

        UserRepository userRepository = new UserRepository();
        userRepository.addUser(chatId, testUser);

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor(
            botCommandService
        );

        SendMessage expectedResponse = new SendMessage(chatId, "Отслеживаем изменения по ссылке: " + url);

        SendMessage actualResponse = botMessageProcessor.processMessage(fakeUpdate);

        assertThat(expectedResponse.getParameters()).isEqualTo(actualResponse.getParameters());
    }

    @Test
    @DisplayName("Проверка команды /track - невалидная ссылка")
    public void testTrackCommandNotValidUrl() {
        String url = "not.a_url";

        Update fakeUpdate = getFakeUpdate("/track " + url);

        BotCommandService botCommandService = Mockito.mock(BotCommandServiceImpl.class);

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor(
            botCommandService
        );

        SendMessage expectedResponse = new SendMessage(chatId, "Введена неверная или пустая ссылка!");

        SendMessage actualResponse = botMessageProcessor.processMessage(fakeUpdate);

        assertThat(expectedResponse.getParameters()).isEqualTo(actualResponse.getParameters());
    }

    @Test
    @DisplayName("Проверка команды /untrack - валидная ссылка")
    public void testUntrackCommand() {
        String url = "https://stackoverflow.com/";

        Update fakeUpdate = getFakeUpdate("/untrack " + url);

        BotCommandService botCommandService = Mockito.mock(BotCommandServiceImpl.class);
        when(botCommandService.removeLink(chatId, url)).thenReturn("Прекращаем отслеживание изменений по ссылке: "
            + url);

        UserRepository userRepository = new UserRepository();
        userRepository.addUser(chatId, testUser);
        userRepository.addLinkToUser(chatId, url);

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor(
            botCommandService
        );

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

        BotCommandService botCommandService = Mockito.mock(BotCommandServiceImpl.class);

        UserRepository userRepository = new UserRepository();
        userRepository.addUser(chatId, testUser);

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor(
            botCommandService
        );

        SendMessage expectedResponse = new SendMessage(chatId, "Введена неверная или пустая ссылка!");

        SendMessage actualResponse = botMessageProcessor.processMessage(fakeUpdate);

        assertThat(expectedResponse.getParameters()).isEqualTo(actualResponse.getParameters());
    }
}
