package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@AllArgsConstructor
public class FilmService {

    private UserStorage userStorage;
    private FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage=userStorage;
    }

    public void addLike(Integer idFilm, Integer idUser) {
        checkFilm(idFilm).addLike(checkUser(idUser).getId());
    }

    public void deleteLike(Integer idFilm, Integer idUser) {
        checkFilm(idFilm).deleteLike(checkUser(idUser).getId());
    }

    public Collection<Film> getMostPopularFilms(int count) {
        Map<Integer, Film> films = filmStorage.getFilms();
        List<Film> mostPopularFilms = new LinkedList<>(films.values());
        List<Film> countPopularFilms = new LinkedList<>();
        if (mostPopularFilms.size() <= 1) return mostPopularFilms;
        if (mostPopularFilms.size() < count) count = mostPopularFilms.size();
        mostPopularFilms.sort((o1, o2) -> o2.getLikes().size() - o1.getLikes().size());
        for (int i = 0; i < count; i++) {
            countPopularFilms.add(mostPopularFilms.get(i));
        }
        return countPopularFilms;
    }

    protected Film checkFilm(Integer idFilm) {
        Map<Integer, Film> films = filmStorage.getFilms();
        if (!films.containsKey(idFilm)) {
            throw new FilmNotFoundException("Фильма с таким id не существует");
        }
        return films.get(idFilm);
    }

    protected User checkUser(Integer idUser) {
        Map<Integer, User> users = userStorage.getUsers();
        if (!users.containsKey(idUser)) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        }
        return users.get(idUser);

    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilm(int id) {
        return filmStorage.getFilm(id);
    }

    public Film createNewFilm(Film film) {
        return filmStorage.createNewFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }
}
