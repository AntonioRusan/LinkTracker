package edu.java.services.tgChat;

import edu.java.controllers.tgChat.TgChatApiController;

/**
 * A delegate to be called by the {@link TgChatApiController}}.
 */

public interface TgChatService {
    default void unregisterChat(Long id) {
    }

    default void registerChat(Long id) {
    }

}
