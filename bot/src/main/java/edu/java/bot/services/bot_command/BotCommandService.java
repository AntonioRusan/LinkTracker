package edu.java.bot.services.bot_command;

public interface BotCommandService {

    String registerChat(Long tgChatId);

    String listLinks(Long tgChatId);

    String addLink(Long tgChatId, String url);

    String removeLink(Long tgChatId, String url);
}
