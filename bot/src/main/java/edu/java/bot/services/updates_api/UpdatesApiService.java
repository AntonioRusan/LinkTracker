package edu.java.bot.services.updates_api;

import api.bot.models.LinkUpdate;
import edu.java.bot.controllers.updates_api.UpdatesApi;
import edu.java.bot.controllers.updates_api.UpdatesApiController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * A delegate to be called by the {@link UpdatesApiController}}.
 */

public interface UpdatesApiService {
    /**
     * POST /updates : Отправить обновление
     *
     * @param linkUpdate (required)
     * @return Обновление обработано (status code 200)
     *     or Некорректные параметры запроса (status code 400)
     * @see UpdatesApi#updatesPost
     */
    default ResponseEntity<Void> updatesPost(LinkUpdate linkUpdate) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
