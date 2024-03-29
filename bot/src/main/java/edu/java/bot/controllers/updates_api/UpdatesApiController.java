package edu.java.bot.controllers.updates_api;

import edu.java.bot.services.updates_api.UpdatesApiService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class UpdatesApiController implements UpdatesApi {

    private final UpdatesApiService delegate;

    public UpdatesApiController(@Autowired(required = false) UpdatesApiService delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new UpdatesApiService() {
        });
    }

    @Override
    public UpdatesApiService getDelegate() {
        return delegate;
    }

}
