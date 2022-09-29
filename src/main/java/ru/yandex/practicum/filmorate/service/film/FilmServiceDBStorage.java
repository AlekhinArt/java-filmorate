package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UnCorrectIDException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@AllArgsConstructor
public class FilmServiceDBStorage implements FilmService {
    //
    private UserStorage userStorage;
    private FilmStorage filmStorage;

    @Autowired
    public FilmServiceDBStorage(@Qualifier("filmDbStorage") FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public void addLike(Integer idFilm, Integer idUser) {
        checkFilm(idFilm);
        checkUser(idUser);
        filmStorage.addLike(idFilm, idUser);
    }

    @Override
    public void deleteLike(Integer idFilm, Integer idUser) {
        checkFilm(idFilm);
        checkUser(idUser);
        filmStorage.deleteLike(idFilm, idUser);
    }
    @Override
    public void deleteAllFilms(){
        filmStorage.deleteAll();
    }
    @Override
    public void deleteFilm(int id){
        checkFilm(id);
        filmStorage.deleteFilm(id);
    }


    @Override
    public Collection<Film> getMostPopularFilms(int count) {
        return filmStorage.getMostPopularFilms(count);
    }

    @Override
    public Film checkFilm(Integer idFilm) {
        Map<Integer, Film> films = filmStorage.getFilms();
        if (!films.containsKey(idFilm)) {
            throw new FilmNotFoundException("Фильма с таким id не существует");
        }
        return films.get(idFilm);
    }

    @Override
    public User checkUser(Integer idUser) {
        Map<Integer, User> users = userStorage.getUsers();
        if (!users.containsKey(idUser)) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        }
        return users.get(idUser);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film getFilm(int id) {
        return filmStorage.getFilm(id);
    }

    @Override
    public Film createNewFilm(Film film) {
        return filmStorage.createNewFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    @Override
    public MpaRating getMpaRating(int id) {
        if (id < 0) {
            throw new UnCorrectIDException("Не корректно введен id: " + id);
        }
        return filmStorage.getMpaRating(id);
    }

    @Override
    public Collection<MpaRating> getMpaRatings() {
        return filmStorage.getMpaRatings();
    }

    @Override
    public Genre getGenre(int id) {
        if (id < 0) {
            throw new UnCorrectIDException("Не корректно введен id: " + id);
        }
        return filmStorage.getGenre(id);
    }

    @Override
    public Collection<Genre> getGenres() {
        return filmStorage.getGenres();
    }
}
