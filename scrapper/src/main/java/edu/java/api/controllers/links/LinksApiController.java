package edu.java.api.controllers.links;

import edu.java.api.services.links.LinksApiService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class LinksApiController implements LinksApi {

    private final LinksApiService delegate;

    public LinksApiController(@Autowired(required = false) LinksApiService delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new LinksApiService() {
        });
    }

    @Override
    public LinksApiService getDelegate() {
        return delegate;
    }

}
