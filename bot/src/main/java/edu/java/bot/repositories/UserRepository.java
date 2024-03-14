package edu.java.bot.repositories;

import edu.java.bot.models.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final static String USER_NOT_REGISTERED = "Вы не зарегистрированы!";
    private final Map<Long, User> chatIdToUser = new HashMap<>();

    public Optional<User> getUserById(Long userId) {
        return Optional.ofNullable(chatIdToUser.get(userId));
    }

    public String addUser(Long userId, User user) {
        if (chatIdToUser.containsKey(userId)) {
            return "Вы уже зарегистрированы!";
        } else {
            chatIdToUser.put(userId, user);
            return "Вы зарегистрированы!";
        }
    }

    public String addLinkToUser(Long userId, String link) {
        Optional<User> userOpt = getUserById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.addLink(link)) {
                return "Отслеживаем изменения по ссылке: " + link;
            } else {
                return "Ссылка уже была добавлена!";
            }
        } else {
            return USER_NOT_REGISTERED;
        }
    }

    public String removeLinkFromUser(Long userId, String link) {
        Optional<User> userOpt = getUserById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.removeLink(link)) {
                return "Прекращаем отслеживание изменений по ссылке: " + link;
            } else {
                return "Эта ссылка не отслеживается!";
            }
        } else {
            return USER_NOT_REGISTERED;
        }
    }

    public List<String> getUserLinks(Long userId) {
        Optional<User> userOpt = getUserById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user.getLinkList();
        } else {
            return new ArrayList<>();
        }
    }

    public Optional<User> getUser(Long userId) {
        if (chatIdToUser.containsKey(userId)) {
            return Optional.of(chatIdToUser.get(userId));
        } else {
            return Optional.empty();
        }
    }

}
