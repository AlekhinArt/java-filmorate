package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.film.FilmServiceDBStorage;

import java.util.Collection;

@Slf4j
@AllArgsConstructor
@RestController
public class MpaRatingController {

    private final FilmServiceDBStorage filmServiceDBStorage;

    @GetMapping("/mpa/{id}")
    public MpaRating getMpaRating(@PathVariable int id) {
        log.debug("Получен Get-запрос к эндпоинту getAllUsers");
        return filmServiceDBStorage.getMpaRating(id);
    }

    @GetMapping("/mpa")
    public Collection<MpaRating> getMpaRatings() {
        log.debug("Получен Get-запрос к эндпоинту getAllUsers");
        return filmServiceDBStorage.getMpaRatings();
    }
}
