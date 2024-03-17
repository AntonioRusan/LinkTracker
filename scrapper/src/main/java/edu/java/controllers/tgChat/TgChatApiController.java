package edu.java.controllers.tgChat;

import edu.java.services.tgChat.TgChatService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class TgChatApiController implements TgChatApi {

    private final TgChatService delegate;

    public TgChatApiController(@Autowired(required = false) TgChatService delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new TgChatService() {
        });
    }

    @Override
    public TgChatService getDelegate() {
        return delegate;
    }

}
