package edu.java.controllers.links;

import edu.java.services.links.LinksService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class LinksApiController implements LinksApi {

    private final LinksService delegate;

    public LinksApiController(@Autowired(required = false) LinksService delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new LinksService() {
        });
    }

    @Override
    public LinksService getDelegate() {
        return delegate;
    }

}
