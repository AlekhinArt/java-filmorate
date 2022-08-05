package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private static final int MAX_LENGTH_DESCRIPTION = 200;
    LocalDate MIN_RELEASE_DATE_FILM = LocalDate.of(1895, 12, 28);
    private int id = 1;

    @GetMapping("/films")
    public Collection<Film> getAllFilms() {
        log.debug("Получен Get-запрос к эндпоинту getAllFilms");
        return films.values();
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) {
        log.debug("Получен Post-запрос к эндпоинту create");
        if (film.getName() == null || film.getName().isBlank()) {
            log.debug("Имя {} не прошло валидацию, пустое или null", film.getName());
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > MAX_LENGTH_DESCRIPTION) {
            log.debug("Описание {} не прошло валидацию, по количеству символов", film.getDescription());
            throw new ValidationException("Описание фильма больше " + MAX_LENGTH_DESCRIPTION + " символов");
        }
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE_FILM)) {
            log.debug("Дата {} не прошла валидацию, слишком ранняя дата выхода фильма", film.getReleaseDate());
            throw new ValidationException("Фильм не может выйти раньше чем " + MIN_RELEASE_DATE_FILM);
        }
        if (film.getDuration() < 0) {
            log.debug("Длительность фильма не может быть <0, введено {}", film.getDuration());
            throw new ValidationException("Длительность фильма не может быть отрицательной");
        }
        film.setId(id);
        films.put(film.getId(), film);
        log.info("Добавлен фильм {}, id хранения {}", film, film.getId());
        id++;
        return film;
    }

    @PutMapping(value = "/films")
    public Film put(@RequestBody Film film) {
        log.debug("Получен Put-запрос к эндпоинту put");
        if (!films.containsKey(film.getId())) {
            log.debug("Фильма с id {} нет", film.getId());
            throw new ValidationException("Фильма с таким id нет");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            log.debug("Имя {} не прошло валидацию, пустое или null", film.getName());
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > MAX_LENGTH_DESCRIPTION) {
            log.debug("Описание {} не прошло валидацию, по количеству символов", film.getDescription());
            throw new ValidationException("Описание фильма больше " + MAX_LENGTH_DESCRIPTION + " символов");
        }
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE_FILM)) {
            log.debug("Дата {} не прошла валидацию, слишком ранняя дата выхода фильма", film.getReleaseDate());
            throw new ValidationException("Фильм не может выйти раньше чем " + MIN_RELEASE_DATE_FILM);
        }
        if (film.getDuration() < 0) {
            log.debug("Длительность фильма не может быть <0, введено {}", film.getDuration());
            throw new ValidationException("Длительность фильма не может быть отрицательной");
        }
        films.put(film.getId(), film);
        log.info("Обновлен фильм {}, id хранения {}", film, film.getId());
        return film;
    }

}
