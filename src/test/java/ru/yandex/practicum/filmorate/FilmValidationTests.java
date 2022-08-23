package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmValidationTests extends InMemoryFilmStorage {
    private Film film;

    @Test
    void createFilmNameIsBlank() {
        film = new Film(1,"","description",
                LocalDate.of(2000, 1, 1), 120);
        assertThrows(ValidationException.class, () -> InMemoryFilmStorage.validation(film));
    }

    @Test
    void createFilmDescriptionMore200() {
        String descr = "d".repeat(201);
        film = new Film(1,"name",descr,
                LocalDate.of(2000, 1, 1), 120);
        assertThrows(ValidationException.class, () -> InMemoryFilmStorage.validation(film));
    }

    @Test
    void createFilmBefore28December1895() {
        film = new Film(1,"name","description",
                LocalDate.of(1895, 12, 27), 120);
        assertThrows(ValidationException.class, () -> InMemoryFilmStorage.validation(film));
    }

    @Test
    void createFilmWithNegativeDuration() {
        film = new Film(1,"name","description",
                LocalDate.of(2000, 1, 1), -1);
        assertThrows(ValidationException.class, () -> InMemoryFilmStorage.validation(film));
    }

}
