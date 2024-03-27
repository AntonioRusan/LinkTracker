package edu.java.repositories;

import edu.java.models.Chat;
import java.util.List;
import java.util.Optional;

public interface ChatRepositoryInterface {

    Optional<Chat> findById(Long id);

    List<Chat> findAll();

    Integer add(Chat chat);

    Integer delete(Long id);
}
