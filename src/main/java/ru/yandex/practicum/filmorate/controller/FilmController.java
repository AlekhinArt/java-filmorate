package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.Constants.FILM_COUNTS_BY_DEFAULT;

@Slf4j
@RestController
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public Collection<Film> getAllFilms() {
        log.debug("Получен Get-запрос к эндпоинту getAllFilms");
        return filmService.getAllFilms();
    }

    @GetMapping("/films/{id}")
    public Film getUser(@PathVariable int id) {
        log.debug("Получен Get-запрос к эндпоинту getAllUsers");
        return filmService.getFilm(id);
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) {
        log.debug("Получен Post-запрос к эндпоинту create");
        return filmService.createNewFilm(film);
    }

    @PutMapping(value = "/films")
    public Film put(@RequestBody Film film) {
        log.debug("Получен Put-запрос к эндпоинту put");
        return filmService.updateFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.debug("Получен Put-запрос к эндпоинту addLike id : {}, userId : {}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.debug("Получен delete-запрос к эндпоинту deleteLike id : {}, userId : {}", id, userId);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public Collection<Film> getPopularFilms(@RequestParam(required = false) String count) {
        log.debug("Получен Get-запрос к эндпоинту getPopularFilms count {}", count);
        if (count != null) {
            return filmService.getMostPopularFilms(Integer.parseInt(count));
        } else return filmService.getMostPopularFilms(FILM_COUNTS_BY_DEFAULT);
    }

}
