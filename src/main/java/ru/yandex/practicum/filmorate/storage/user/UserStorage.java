package ru.yandex.practicum.filmorate.storage.user;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {

    public List<User> getUsers();

    public User createUser(User user);

    public User updateUser(User newUser);

    public Optional<User> getUserById(Long id);

    public List<User> getUserFriends(Long id);

    public List<User> getCommonFriends(Long userId, Long otherId);

    public void addFriend(Long userId, Long friendId);

    public void deleteFriend(Long userId, Long friendsId);

    public Set<Long> getUserFriendsId(Long id);

    public boolean checkIfUserExists(Long id);
}
