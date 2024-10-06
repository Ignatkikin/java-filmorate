package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public List<User> getUsers() {
        log.info("Запрос на получение всех пользователей");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validateUser(user);
        user.setId(getNextUserId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Добавление. Пустое имя пользователя, использован Логин {}", user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Пользователь {}  c id {} успешно добавлен", user.getLogin(), user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        if (newUser.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            validateUser(newUser);
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
        throw new ValidationException("пользователь с id " + newUser.getId() + " не найден");
    }

    private void validateUser(@Valid @RequestBody User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.error("Email не должен быть пустым и должен содержать символ @");
            throw new ValidationException("Email не должен быть пустым и должен содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.error("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

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
