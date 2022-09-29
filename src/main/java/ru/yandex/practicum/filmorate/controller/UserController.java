package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserServiceDBStorage;

import java.util.Collection;

@Slf4j
@AllArgsConstructor
@RestController
public class UserController {

    private final UserServiceDBStorage userServiceDBStorage;

    @GetMapping("/users")
    public Collection<User> getAllUsers() {
        log.debug("Получен Get-запрос к эндпоинту getAllUsers");
        return userServiceDBStorage.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
        log.debug("Получен Get-запрос к эндпоинту getUser, id : {}", id);
        return userServiceDBStorage.getUser(id);
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) {
        log.debug("Получен Post-запрос к эндпоинту create");

        return userServiceDBStorage.createNewUser(user);
    }

    @PutMapping(value = "/users")
    public User put(@RequestBody User user) {
        log.debug("Получен put-запрос к эндпоинту put");
        return userServiceDBStorage.updateUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Получен put-запрос к эндпоинту addFriend, id : {}, friendId : {}", id, friendId);
        userServiceDBStorage.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Получен delete-запрос к эндпоинту deleteFriend, id : {}, friendId : {}", id, friendId);
        userServiceDBStorage.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> getAllUserFriends(@PathVariable int id) {
        log.debug("Получен get-запрос к эндпоинту getAllUserFriends, id : {}", id);
        return userServiceDBStorage.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<User> getUserFriends(@PathVariable int id, @PathVariable int otherId) {
        log.debug("Получен get-запрос к эндпоинту getUserFriends, id : {}, otherId : {}", id, otherId);
        return userServiceDBStorage.getCommonFriend(id, otherId);
    }

    @DeleteMapping("/users")
    public void deleteAllUsers() {
        log.debug("Получен delete-запрос к эндпоинту deleteAllUsers");
        userServiceDBStorage.deleteAllUsers();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        log.debug("Получен delete-запрос к эндпоинту deleteUser, id : {}", id);
        userServiceDBStorage.deleteUser(id);
    }

}
