package ru.yandex.practicum.filmorate.storage.user;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    public List<User> getUsers();

    public User createUser(User user);

    public User updateUser(User newUser);

    public User getUserById(Long id);

    public List<User> getUserFriends(Long id);

    public List<User> getCommonFriends(Long userId, Long otherId);

    public void addFriend(Long userId, Long friendId);

    public void deleteFriend(Long userId, Long friendsId);
}
