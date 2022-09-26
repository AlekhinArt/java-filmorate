package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;
import java.util.Map;


@Component
public interface FilmStorage {

    Film getFilm(int id);

    Film createNewFilm(Film film);

    Collection<Film> getAllFilms();

    Film updateFilm(Film film);

    Map<Integer, Film> getFilms();


    void addLike(Integer idFilm, Integer idUser);

    void deleteLike(Integer idFilm, Integer idUser);

    Collection<Film> getMostPopularFilms(int count);


    MpaRating getMpaRating(int id);

    Collection<MpaRating> getMpaRatings();

    Genre getGenre(int id);

    Collection<Genre> getGenres();

    void deleteAll();
}
