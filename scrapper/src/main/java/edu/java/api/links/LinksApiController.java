package edu.java.api.links;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class LinksApiController implements LinksApi {

    private final LinksApiDelegate delegate;

    public LinksApiController(@Autowired(required = false) LinksApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new LinksApiDelegate() {
        });
    }

    @Override
    public LinksApiDelegate getDelegate() {
        return delegate;
    }

}
