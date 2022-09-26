package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j

public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public void addFriend(int id, int friendId) {

    }

    @Override
    public void deleteFriend(int id, int friendId) {

    }

    @Override
    public User getUser(int id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Нет пользователя с таким id");
        }
        return users.get(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User createNewUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        validation(user);
        user.setId(id);
        users.put(user.getId(), user);
        id++;
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException("Нет пользователя с таким id");
        }
        validation(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }

    @Override
    public void deleteAll() {

    }


    protected static void validation(User user) {
        if (user.getId() < 0) {
            throw new ValidationException("id не может быть меньше 0");
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.debug("email {} не прошел валидацию", user.getEmail());
            throw new ValidationException("Введен не корректный email");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.debug("login {} не прошел валидацию", user.getLogin());
            throw new ValidationException("Введен не корректный логин");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Дата рождения  {} не прошла валидацию, позже настоящего времени или null", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем или null");
        }
    }
}


