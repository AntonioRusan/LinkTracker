package edu.java.services.tgChat;

import edu.java.controllers.tgChat.TgChatApi;
import edu.java.controllers.tgChat.TgChatApiController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * A delegate to be called by the {@link TgChatApiController}}.
 */

public interface TgChatService {

    /**
     * DELETE /tg-chat/{id} : Удалить чат
     *
     * @param id (required)
     * @return Чат успешно удалён (status code 200)
     *     or Некорректные параметры запроса (status code 400)
     *     or Чат не существует (status code 404)
     * @see TgChatApi#tgChatIdDelete
     */
    default ResponseEntity<Void> unregisterChat(Long id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * POST /tg-chat/{id} : Зарегистрировать чат
     *
     * @param id (required)
     * @return Чат зарегистрирован (status code 200)
     *     or Некорректные параметры запроса (status code 400)
     * @see TgChatApi#tgChatIdPost
     */
    default ResponseEntity<Void> registerChat(Long id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
