package edu.java.controllers.tgChat;

import edu.java.services.tgChat_api.TgChatApiService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class TgChatApiController implements TgChatApi {

    private final TgChatApiService delegate;

    public TgChatApiController(@Autowired(required = false) TgChatApiService delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new TgChatApiService() {
        });
    }

    @Override
    public TgChatApiService getDelegate() {
        return delegate;
    }

}
