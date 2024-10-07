package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    UserController userController = new UserController();


    @Test
    void createUser() {
        User user = User.builder()
                .email("IgnatKikin@yandex.ru")
                .login("IgnatKikin")
                .name("Ignat")
                .birthday(LocalDate.of(1995, 11, 22))
                .build();
        userController.createUser(user);
        List<User> users = userController.getUsers();
        assertEquals("IgnatKikin", users.get(0).getLogin());
    }

    @Test
    void testUpdateUser() {
        User user = User.builder()
                .email("IgnatKikin@yandex.ru")
                .login("IgnatKikin")
                .name("Ignat")
                .birthday(LocalDate.of(1995, 11, 22))
                .build();
        userController.createUser(user);

        User user1 = User.builder()
                .id(1L)
                .email("VovaVovachkin@yandex.ru")
                .login("VovaVovachkin")
                .name("Vova")
                .birthday(LocalDate.of(2005, 11, 22))
                .build();
        userController.updateUser(user1);
        List<User> users = userController.getUsers();
        assertEquals("VovaVovachkin", users.get(0).getLogin());
    }

    @Test
    void testGetUsers() {
        User user = User.builder()
                .email("VovaVovachkin@yandex.ru")
                .login("VovaVovachkin")
                .name("Vova")
                .birthday(LocalDate.of(2005, 11, 22))
                .build();
        userController.createUser(user);
        User user1 = User.builder()
                .email("IgnatKikin@yandex.ru")
                .login("IgnatKikin")
                .name("Ignat")
                .birthday(LocalDate.of(1995, 11, 22))
                .build();
        userController.createUser(user1);

        List<User> users = userController.getUsers();
        assertEquals(2, users.size());
    }

    @Test
    void testCreateUserWhenNameIsEmptyWriteLogin() {
        User user = User.builder()
                .email("IgnatKikin@yandex.ru")
                .login("IgnatKikin")
                .birthday(LocalDate.of(1995, 11, 22))
                .build();
        userController.createUser(user);
        List<User> users = userController.getUsers();
        assertEquals("IgnatKikin", users.get(0).getName());
    }
}