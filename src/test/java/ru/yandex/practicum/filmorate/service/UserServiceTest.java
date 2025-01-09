package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserRepository.class, UserRowMapper.class, UserService.class})
public class UserServiceTest {

    @Autowired
    UserService userService;

    void createUser() {
        User user = User.builder()
                .email("IgnatKikin@yandex.ru")
                .login("IgnatKikin")
                .name("Ignat")
                .birthday(LocalDate.of(1995, 11, 22))
                .friends(new HashSet<>())
                .build();
        userService.createUser(user);

        User user1 = User.builder()
                .email("VovaVovan@yandex.ru")
                .login("VovaVovan")
                .name("Vova")
                .birthday(LocalDate.of(2000, 7, 10))
                .friends(new HashSet<>())
                .build();
        userService.createUser(user1);

        User user2 = User.builder()
                .email("StepanBulkin@yandex.ru")
                .login("StepanBulkin")
                .name("Stepan")
                .birthday(LocalDate.of(1990, 5, 8))
                .friends(new HashSet<>())
                .build();
        userService.createUser(user2);

        User user3 = User.builder()
                .email("KolyaBurya@yandex.ru")
                .login("KolyaBurya")
                .name("Kolya")
                .birthday(LocalDate.of(2002, 3, 18))
                .friends(new HashSet<>())
                .build();
        userService.createUser(user3);
    }

    @Test
    @DirtiesContext
    void testUpdateUser() {
        createUser();
        User user4 = User.builder()
                .id(1L)
                .email("VovaVovachkin@yandex.ru")
                .login("VovaVovachkin")
                .name("Vova")
                .birthday(LocalDate.of(2005, 11, 22))
                .friends(new HashSet<>())
                .build();
        userService.updateUser(user4);

        List<User> users = userService.getUsers();
        assertEquals("VovaVovachkin", users.get(0).getLogin());
    }

    @Test
    @DirtiesContext
    void testGetUsers() {
        createUser();
        List<User> users = userService.getUsers();
        assertEquals(4, users.size());
    }

    @Test
    @DirtiesContext
    void testCreateUserWhenNameIsEmptyWriteLogin() {
        User user = User.builder()
                .email("IgnatKikin@yandex.ru")
                .login("IgnatKikin")
                .birthday(LocalDate.of(1995, 11, 22))
                .friends(new HashSet<>())
                .build();

        userService.createUser(user);
        List<User> users = userService.getUsers();
        assertEquals("IgnatKikin", users.get(0).getName());
    }

    @Test
    @DirtiesContext
    void testAddFriends() {
        createUser();
        userService.addFriend(1L, 2L);
        List<User> friends = userService.getUserFriends(1L);
        assertEquals(2, friends.get(0).getId());
    }

    @Test
    @DirtiesContext
    void testGetUserById() {
        createUser();
        User user = userService.getUserById(3L);
        assertEquals("Stepan", user.getName());
    }

    @Test
    @DirtiesContext
    void testDeleteFriends() {
        createUser();
        userService.addFriend(1L, 2L);
        User user1 = userService.getUserById(1L);
        userService.deleteFriend(1L, 2L);
        User user2 = userService.getUserById(1L);
        List<User> friends = userService.getUserFriends(1L);
        assertTrue(friends.isEmpty());
    }

    @Test
    @DirtiesContext
    void testGetCommonFriends() {
        createUser();
        userService.addFriend(1L, 4L);
        userService.addFriend(2L, 4L);
        List<User> friends = userService.getCommonFriends(1L, 2L);
        assertEquals(4, friends.get(0).getId());
    }
}
