package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        return userStorage.createUser(user);
    }

    public User updateUser(User newUser) {
        log.info("Запрос на обновление пользователя");
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

    public List<User> getAllFriends(Long id) {
        log.info("Запрос на получение всех друзей пользователя с id {}", id);
        List<User> friendsList = new ArrayList<>();
        User user = userStorage.getUserById(id);

        for (Long friendId : user.getFriends()) {
            User friend = userStorage.getUserById(friendId);
            friendsList.add(friend);
        }
        return friendsList;
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        log.info("Запрос на общих друзей пользователя {} с пользователем {}", userId, otherId);
        User user = userStorage.getUserById(userId);
        User otherUser = userStorage.getUserById(otherId);

        return user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}
