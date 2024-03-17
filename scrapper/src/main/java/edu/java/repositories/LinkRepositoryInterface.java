package edu.java.repositories;

import edu.java.models.Link;
import java.util.List;
import java.util.Optional;

public interface LinkRepositoryInterface {
    Optional<Link> findById(Long id);

    Optional<Link> findByUrl(String url);

    List<Link> findAll();

    Long add(Link chat);

    Integer delete(Long id);
}
