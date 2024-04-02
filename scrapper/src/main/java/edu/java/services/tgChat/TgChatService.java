package edu.java.services.tgChat;

import edu.java.controllers.tgChat.TgChatApiController;

/**
 * A delegate to be called by the {@link TgChatApiController}}.
 */

public interface TgChatService {
    void unregisterChat(Long id);

    void registerChat(Long id);

}
