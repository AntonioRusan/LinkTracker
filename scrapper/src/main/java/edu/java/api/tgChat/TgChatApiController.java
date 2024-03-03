package edu.java.api.tgChat;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api")
public class TgChatApiController implements TgChatApi {

    private final TgChatApiDelegate delegate;

    public TgChatApiController(@Autowired(required = false) TgChatApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new TgChatApiDelegate() {
        });
    }

    @Override
    public TgChatApiDelegate getDelegate() {
        return delegate;
    }

}
