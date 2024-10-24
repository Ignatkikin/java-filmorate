package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> getUsers() {
        log.info("Запрос на получение всех пользователей");
        return userStorage.getUsers();
    }

    public User createUser(User user) {
        log.info("Запрос на добавление пользователя");
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Добавление. Пустое имя пользователя, использован Логин {}", user.getLogin());
        }
        return userStorage.createUser(user);
    }

    public User updateUser(User newUser) {
        log.info("Запрос на обновление пользователя");
        if (newUser.getId() == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
            log.info("Обновление. Пустое имя пользователя, использован Логин {}", newUser.getLogin());
        }
        return userStorage.updateUser(newUser);
    }

    public User getUserById(Long id) {
        log.info("Запрос на получение пользователя с Id {}", id);
        return userStorage.getUserById(id);
    }

    public void addFriend(Long id, Long friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(id);
        log.info("Пользователь с Id {} добавил в друзья пользователя с Id {}", id, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friends = userStorage.getUserById(friendId);

        user.getFriends().remove(friendId);
        friends.getFriends().remove(userId);
        log.info("Пользователь с Id {} удалил из друзей пользователя с Id {}", userId, friendId);
    }

    public List<User> getUserFriends(Long id) {
        log.info("Запрос на получение всех друзей пользователя с id {}", id);
        return userStorage.getUserFriends(id);
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        log.info("Запрос на общих друзей пользователя {} с пользователем {}", userId, otherId);
        return userStorage.getCommonFriends(userId, otherId);
    }
}
