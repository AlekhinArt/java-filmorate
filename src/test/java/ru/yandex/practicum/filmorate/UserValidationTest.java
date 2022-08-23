package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidationTest extends InMemoryUserStorage {

    User user;

    @Test
    void createUserWithIsEmptyEmail() {
        user = new User(1,"Test Login","",
                "Test Name",LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> InMemoryUserStorage.validation(user));
    }

    @Test
    void createUserWithUncorrectedEmail() {
        user = new User(1,"Test Login","testemail",
                "Test Name",LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> InMemoryUserStorage.validation(user));
    }

    @Test
    void createUserWithIsEmptyLogin() {
        user = new User(1,"","testemail@yandex.ru",
                "Test Name",LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> InMemoryUserStorage.validation(user));
    }

    @Test
    void createUserWithFutureBirthdayTime() {
        user = new User(1,"test","testemail@yandex.ru",
                "Test Name",LocalDate.now().plusDays(10));
        assertThrows(ValidationException.class, () -> InMemoryUserStorage.validation(user));
    }
}
