package ru.yandex.practicum.filmorate.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.film.FilmServiceDBStorage;

import java.util.Collection;

@Slf4j
@AllArgsConstructor
@RestController
public class GenreController {
    private final FilmServiceDBStorage filmServiceDBStorage;

    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable int id) {
        log.debug("Получен Get-запрос к эндпоинту getAllUsers");
        return filmServiceDBStorage.getGenre(id);
    }

    @GetMapping("/genres")
    public Collection<Genre> getGenres() {
        log.debug("Получен Get-запрос к эндпоинту getAllUsers");
        return filmServiceDBStorage.getGenres();
    }

}
