package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;


@Component
public interface FilmStorage {

    Film getFilm(int id);
    Film createNewFilm(Film film);

    Collection<Film> getAllFilms();

    Film updateFilm(Film film);
    Map<Integer, Film> getFilms();


}
