package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FilmService {
    void addLike(Integer idFilm, Integer idUser);

    void deleteLike(Integer idFilm, Integer idUser);

    void deleteAllFilms();

    void deleteFilm(int id);

    Collection<Film> getMostPopularFilms(int count);

    Film checkFilm(Integer idFilm);

    User checkUser(Integer idUser);

    Collection<Film> getAllFilms();

    Film getFilm(int id);

    Film createNewFilm(Film film);

    Film updateFilm(Film film);

    MpaRating getMpaRating(int id);

    Collection<MpaRating> getMpaRatings();

    Genre getGenre(int id);

    Collection<Genre> getGenres();
}
