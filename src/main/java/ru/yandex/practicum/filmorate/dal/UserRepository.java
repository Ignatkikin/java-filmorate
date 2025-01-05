package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Slf4j
public class UserRepository extends BaseRepository<User> implements UserStorage {
    private static final String GET_ALL_USERS_QUERY = "SELECT * FROM users ORDER BY user_id";
    private static final String INSERT_USER_QUERY = "INSERT INTO users (email, login, name, birthday) VALUES (?,?,?,?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users " +
            "SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
    private static final String GET_USER_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String GET_USER_FRIENDS_QUERY = "SELECT u.* " +
            "FROM users AS u " +
            "JOIN friends AS f ON u.user_id = f.friends_id WHERE f.user_id = ?";
    private static final String GET_COMMON_FRIENDS_QUERY = "SELECT u.* " +
            "FROM users AS u " +
            "JOIN friends AS f ON u.user_id = f.friends_id " +
            "JOIN friends AS fr ON u.user_id = fr.friends_id " +
            "WHERE f.user_id = ? AND fr.user_id = ?";
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO friends (user_id, friends_id) VALUES (?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = ? AND friends_id = ?";
    private static final String GET_ID_FRIENDS_QUERY = "SELECT friends_id FROM friends WHERE user_id = ?";


    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<User> getUsers() {
        List<User> users = findMany(GET_ALL_USERS_QUERY);
        for (User user : users) {
            user.setFriends(getUserFriendsId(user.getId()));
        }
        return users;
    }

    @Override
    public User createUser(User user) {
        Long id = insert(INSERT_USER_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        user.setId(id);
        log.info("Пользователь {} c id {} успешно добавлен", user.getEmail(), user.getId());
        return user;
    }

    @Override
    public User updateUser(User newUser) {
        update(UPDATE_USER_QUERY,
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday(),
                newUser.getId());
        log.info("Пользователь {} c id {} успешно обнавлен", newUser.getEmail(), newUser.getId());
        return newUser;
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> userOptional = findOne(GET_USER_BY_ID_QUERY, id);
        User user = userOptional.orElseThrow(() -> new NotFoundException("пользователь с id " + id + " не найден"));
        user.setFriends(getUserFriendsId(user.getId()));
        return user;
    }

    @Override
    public List<User> getUserFriends(Long id) {
        return findMany(GET_USER_FRIENDS_QUERY, id);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherId) {
        return findMany(GET_COMMON_FRIENDS_QUERY, userId, otherId);
    }

    public Set<Long> getUserFriendsId(Long id) {
        return new HashSet<>(findManyId(GET_ID_FRIENDS_QUERY, id));
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        update(INSERT_FRIEND_QUERY, userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendsId) {
        jdbc.update(DELETE_FRIEND_QUERY, userId, friendsId);
    }
}
