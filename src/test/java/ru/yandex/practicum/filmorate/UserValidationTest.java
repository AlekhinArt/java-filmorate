package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidationTest extends UserController {

    User user;

    @Test
    void createUserWithIsEmptyEmail() {
        user = User.builder()
                .id(1)
                .login("Test Login")
                .name("Test Name")
                .email("")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        assertThrows(ValidationException.class, () -> UserController.validation(user));
    }

    @Test
    void createUserWithUncorrectedEmail() {
        user = User.builder()
                .id(1)
                .login("Test Login")
                .name("Test Name")
                .email("testemail")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        assertThrows(ValidationException.class, () -> UserController.validation(user));
    }

    @Test
    void createUserWithIsEmptyLogin() {
        user = User.builder()
                .id(1)
                .login("")
                .name("Test Name")
                .email("testemail@yandex.ru")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        assertThrows(ValidationException.class, () -> UserController.validation(user));
    }

    @Test
    void createUserWithFutureBirthdayTime() {
        user = User.builder()
                .id(1)
                .login("test")
                .name("Test Name")
                .email("testemail@yandex.ru")
                .birthday(LocalDate.now().plusDays(10))
                .build();
        assertThrows(ValidationException.class, () -> UserController.validation(user));
    }
}
