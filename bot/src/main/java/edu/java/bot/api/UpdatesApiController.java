package edu.java.bot.api;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class UpdatesApiController implements UpdatesApi {

    private final UpdatesApiDelegate delegate;

    public UpdatesApiController(@Autowired(required = false) UpdatesApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new UpdatesApiDelegate() {
        });
    }

    @Override
    public UpdatesApiDelegate getDelegate() {
        return delegate;
    }

}
