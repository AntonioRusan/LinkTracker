package edu.java.services.links;

import api.scrapper.models.AddLinkRequest;
import api.scrapper.models.LinkResponse;
import api.scrapper.models.ListLinksResponse;
import api.scrapper.models.RemoveLinkRequest;
import edu.java.controllers.links.LinksApi;
import edu.java.controllers.links.LinksApiController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * A delegate to be called by the {@link LinksApiController}}.
 */

@SuppressWarnings("MultipleStringLiterals")
public interface LinksService {

    /**
     * DELETE /links : Убрать отслеживание ссылки
     *
     * @param tgChatId          (required)
     * @param removeLinkRequest (required)
     * @return Ссылка успешно убрана (status code 200)
     *     or Некорректные параметры запроса (status code 400)
     *     or Ссылка не найдена (status code 404)
     * @see LinksApi#linksDelete
     */
    default ResponseEntity<LinkResponse> removeLink(
        Long tgChatId,
        RemoveLinkRequest removeLinkRequest
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * GET /links : Получить все отслеживаемые ссылки
     *
     * @param tgChatId (required)
     * @return Ссылки успешно получены (status code 200)
     *     or Некорректные параметры запроса (status code 400)
     * @see LinksApi#linksGet
     */
    default ResponseEntity<ListLinksResponse> getAllLinks(Long tgChatId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * POST /links : Добавить отслеживание ссылки
     *
     * @param tgChatId       (required)
     * @param addLinkRequest (required)
     * @return Ссылка успешно добавлена (status code 200)
     *     or Некорректные параметры запроса (status code 400)
     * @see LinksApi#linksPost
     */
    default ResponseEntity<LinkResponse> addLink(
        Long tgChatId,
        AddLinkRequest addLinkRequest
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
