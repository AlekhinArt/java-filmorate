package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @GetMapping("/users")
    public Collection<User> getAllUsers() {
        log.debug("Получен Get-запрос к эндпоинту getAllUsers");
        return users.values();
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) {
        log.debug("Получен Post-запрос к эндпоинту create");
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.debug("email {} не прошел валидацию", user.getEmail());
            throw new ValidationException("Введен не корректный email");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.debug("login {} не прошел валидацию", user.getLogin());
            throw new ValidationException("Введен не корректный логин");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Дата рождения  {} не прошла валидацию, позже настоящего времени", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(id);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь {}, с id {}", user, user.getId());
        id++;
        return user;
    }

    @PutMapping(value = "/users")
    public User put(@RequestBody User user) {
        log.debug("Получен put-запрос к эндпоинту put");
        if (!users.containsKey(user.getId())) {
            log.debug("Пользователя с id {} нет", user.getId());
            throw new ValidationException("Нет пользователя с таким id");
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.debug("email {} не прошел валидацию", user.getEmail());
            throw new ValidationException("Введен не корректный email");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.debug("login {} не прошел валидацию", user.getLogin());
            throw new ValidationException("Введен не корректный логин");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Дата рождения  {} не прошла валидацию, позже настоящего времени", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Обновлены данные пользователя {}, с id {}", user, user.getId());
        return user;
    }


}
