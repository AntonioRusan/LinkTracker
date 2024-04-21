/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.1.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package edu.java.controllers.tgChat;

import api.models.ApiErrorResponse;
import edu.java.exceptions.api.LinksApiException;
import edu.java.exceptions.api.base.ApiException;
import edu.java.exceptions.api.base.BadRequestException;
import edu.java.exceptions.api.base.ConflictException;
import edu.java.exceptions.api.base.NotFoundException;
import edu.java.services.tgChat.TgChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/api")
@Validated
@Tag(name = "tg-chat", description = "the tg-chat API")
@RequiredArgsConstructor
public class TgChatApiController {
    private final TgChatService tgChatService;
    private final List<Class<? extends ApiException>> apiExceptionClassList =
        List.of(NotFoundException.class, BadRequestException.class, ConflictException.class, NotFoundException.class);

    /**
     * DELETE /tg-chat/{id} : Удалить чат
     *
     * @param id (required)
     * @return Чат успешно удалён (status code 200)
     *     or Некорректные параметры запроса (status code 400)
     *     or Чат не существует (status code 404)
     */
    @Operation(
        operationId = "tgChatIdDelete",
        summary = "Удалить чат",
        responses = {
            @ApiResponse(responseCode = "200", description = "Чат успешно удалён"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class)
                )
            }),
            @ApiResponse(responseCode = "404", description = "Чат не существует", content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class)
                )
            })
        }
    )
    @RequestMapping(
        method = RequestMethod.DELETE,
        value = "/tg-chat/{id}",
        produces = {"application/json"}
    )
    ResponseEntity<Void> tgChatIdDelete(
        @Parameter(name = "id", description = "", required = true, in = ParameterIn.PATH)
        @PathVariable("id") Long id
    ) {
        try {
            tgChatService.unregisterChat(id);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            if (!apiExceptionClassList.contains(ex.getClass())) {
                throw new LinksApiException(ex.getMessage());
            } else {
                throw ex;
            }
        }
    }

    /**
     * POST /tg-chat/{id} : Зарегистрировать чат
     *
     * @param id (required)
     * @return Чат зарегистрирован (status code 200)
     *     or Некорректные параметры запроса (status code 400)
     *     or Повторная регистрация чата (status code 409)
     */
    @Operation(
        operationId = "tgChatIdPost",
        summary = "Зарегистрировать чат",
        responses = {
            @ApiResponse(responseCode = "200", description = "Чат зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class)
                )
            }),
            @ApiResponse(responseCode = "409", description = "Повторная регистрация чата", content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class)
                )
            })
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/tg-chat/{id}",
        produces = {"application/json"}
    )
    ResponseEntity<Void> tgChatIdPost(
        @Parameter(name = "id", description = "", required = true, in = ParameterIn.PATH)
        @PathVariable("id") Long id
    ) {
        try {
            tgChatService.registerChat(id);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            if (!apiExceptionClassList.contains(ex.getClass())) {
                throw new LinksApiException(ex.getMessage());
            } else {
                throw ex;
            }
        }

    }

}
