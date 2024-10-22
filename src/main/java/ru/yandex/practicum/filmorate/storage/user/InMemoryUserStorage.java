package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

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
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Добавление. Пустое имя пользователя, использован Логин {}", user.getLogin());
        }
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        log.info("Пользователь {}  c id {} успешно добавлен", user.getLogin(), user.getId());
        return user;
    }

    @Override
    public User updateUser(User newUser) {
        if (newUser.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            if (newUser.getName() == null || newUser.getName().isBlank()) {
                oldUser.setName(newUser.getLogin());
                log.info("Обновление. Пустое имя пользователя, использован Логин {}", newUser.getLogin());
            } else {
                oldUser.setName(newUser.getName());
            }
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

    private long getNextUserId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
