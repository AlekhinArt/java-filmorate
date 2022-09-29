package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserService {
    void addFriend(int id, int friendId);

    void deleteFriend(int id, int friendId);

    Collection<User> getCommonFriend(int id, int friendId);

    Collection<User> getFriends(int id);

    Collection<User> getAllUsers();

    User getUser(int id);

    User createNewUser(User user);

    User updateUser(User user);

    void deleteUser(int id);

    void deleteAllUsers();
}
