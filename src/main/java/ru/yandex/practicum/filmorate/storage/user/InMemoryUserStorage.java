package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        user.setId(getNextUserId());
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        log.info("Пользователь {}  c id {} успешно добавлен", user.getLogin(), user.getId());
        return user;
    }

    @Override
    public User updateUser(User newUser) {
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());
            log.info("Пользователь {} c id {} успешно обновлен", oldUser.getLogin(), oldUser.getId());
            return oldUser;
        }
        log.error("пользователь с id {} не найден", newUser.getId());
        throw new NotFoundException("пользователь с id " + newUser.getId() + " не найден");
    }

    @Override
    public User getUserById(Long id) {
        if (users.get(id) == null) {
            throw new NotFoundException("пользователь с id " + id + " не найден");
        }
        return users.get(id);
    }

    @Override
    public List<User> getUserFriends(Long id) {
        List<User> friendsList = new ArrayList<>();
        User user = getUserById(id);

        for (Long friendId : user.getFriends()) {
            User friend = getUserById(friendId);
            friendsList.add(friend);
        }
        return friendsList;
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherId) {
        User user = getUserById(userId);
        User otherUser = getUserById(otherId);

        return user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    private long getNextUserId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
