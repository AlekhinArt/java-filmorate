package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

@Component
public interface UserStorage {

    void addFriend(int id, int friendId);

    void deleteFriend(int id, int friendId);

    User getUser(int id);

    Collection<User> getAllUsers();

    User createNewUser(User user);

    User updateUser(User user);

    Map<Integer, User> getUsers();

    void deleteAll();
}
