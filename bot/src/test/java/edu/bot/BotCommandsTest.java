package edu.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.BotMessageProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

public class BotCommandsTest {
    private final Long chatId = 1L;

    private Update getFakeUpdate(String fakeMessageText) {

        Update fakeUpdate = Mockito.mock(Update.class);
        Message fakeMessage = Mockito.mock(Message.class);
        Chat fakeChat = Mockito.mock(Chat.class);

        when(fakeChat.id()).thenReturn(chatId);

        when(fakeMessage.chat()).thenReturn(fakeChat);
        when(fakeMessage.text()).thenReturn(fakeMessageText);

        when(fakeUpdate.message()).thenReturn(fakeMessage);
        return fakeUpdate;
    }

    @Test
    @DisplayName("Проверка команды регистрации /start")
    public void testStartCommand() {
        String userFirstName = "John";
        String userLastName = "Snow";
        Long chatId = 1L;

        Update fakeUpdate = Mockito.mock(Update.class);
        Message fakeMessage = Mockito.mock(Message.class);
        Chat fakeChat = Mockito.mock(Chat.class);

        when(fakeChat.id()).thenReturn(chatId);
        when(fakeChat.firstName()).thenReturn(userFirstName);
        when(fakeChat.lastName()).thenReturn(userLastName);

        when(fakeMessage.chat()).thenReturn(fakeChat);
        when(fakeMessage.text()).thenReturn("/start");

        when(fakeUpdate.message()).thenReturn(fakeMessage);

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor();

        SendMessage expectedResponse = new SendMessage(chatId, "Привет, " + userFirstName
            + " " + userLastName + "!\nВы зарегистрированы!");

        SendMessage actualResponse = botMessageProcessor.processMessage(fakeUpdate);

        assertThat(expectedResponse.getParameters()).isEqualTo(actualResponse.getParameters());
    }

    @Test
    @DisplayName("Проверка команды /list - нет отслеживаемых ссылок")
    public void testListCommand() {
        Update fakeUpdate = getFakeUpdate("/list");

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor();

        SendMessage expectedResponse = new SendMessage(chatId, "Нет отслеживаемых ссылок!");

        SendMessage actualResponse = botMessageProcessor.processMessage(fakeUpdate);

        assertThat(expectedResponse.getParameters()).isEqualTo(actualResponse.getParameters());
    }

    @Test
    @DisplayName("Проверка команды /help")
    public void testHelpCommand() {
        Update fakeUpdate = getFakeUpdate("/help");

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor();

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

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor();

        SendMessage expectedResponse = new SendMessage(chatId, "Отслеживаем изменения по ссылке: " + url);

        SendMessage actualResponse = botMessageProcessor.processMessage(fakeUpdate);

        assertThat(expectedResponse.getParameters()).isEqualTo(actualResponse.getParameters());
    }

    @Test
    @DisplayName("Проверка команды /track - невалидная ссылка")
    public void testTrackCommandNotValidUrl() {
        String url = "not.a_url";

        Update fakeUpdate = getFakeUpdate("/track " + url);

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor();

        SendMessage expectedResponse = new SendMessage(chatId, "Введена неверная или пустая ссылка!");

        SendMessage actualResponse = botMessageProcessor.processMessage(fakeUpdate);

        assertThat(expectedResponse.getParameters()).isEqualTo(actualResponse.getParameters());
    }

    @Test
    @DisplayName("Проверка команды /untrack - валидная ссылка")
    public void testUnTrackCommand() {
        String url = "https://stackoverflow.com/";

        Update fakeUpdate = getFakeUpdate("/untrack " + url);

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor();

        SendMessage expectedResponse = new SendMessage(chatId, "Прекращаем отслеживание изменений по ссылке: "
            + url);

        SendMessage actualResponse = botMessageProcessor.processMessage(fakeUpdate);

        assertThat(expectedResponse.getParameters()).isEqualTo(actualResponse.getParameters());
    }

    @Test
    @DisplayName("Проверка команды /untrack - невалидная ссылка")
    public void testUnTrackCommandNotValidUrl() {
        String url = "not.a_url";

        Update fakeUpdate = getFakeUpdate("/untrack " + url);

        BotMessageProcessor botMessageProcessor = new BotMessageProcessor();

        SendMessage expectedResponse = new SendMessage(chatId, "Введена неверная или пустая ссылка!");

        SendMessage actualResponse = botMessageProcessor.processMessage(fakeUpdate);

        assertThat(expectedResponse.getParameters()).isEqualTo(actualResponse.getParameters());
    }
}
