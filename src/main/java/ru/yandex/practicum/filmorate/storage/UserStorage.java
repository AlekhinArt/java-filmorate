package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Component
public interface UserStorage {

    User getUser(int id);
    Collection<User> getAllUsers();

    User createNewUser(User user);

    User updateUser(User user);
}
