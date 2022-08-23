package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;


@Service
public class FilmService {

    InMemoryFilmStorage filmStorage;
    InMemoryUserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.filmStorage = inMemoryFilmStorage;
        this.userStorage = inMemoryUserStorage;
    }

    public void addLike(Integer idFilm, Integer idUser) {
        checkFilm(idFilm).addLike(checkUser(idUser));
    }

    public void deleteLike(Integer idFilm, Integer idUser) {
        checkFilm(idFilm).deleteLike(checkUser(idUser));
    }

    public Collection<Film> getMostPopularFilms(int count) {
        Map<Integer, Film> films = filmStorage.getFilms();
        List<Film> mostPopularFilms = new LinkedList<>(films.values());
        List<Film> countPopularFilms = new LinkedList<>();
        if (mostPopularFilms.size() <= 1) return mostPopularFilms;
        if (mostPopularFilms.size() < count) count = mostPopularFilms.size();
        mostPopularFilms.sort(new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                return o2.getLikes().size() - o1.getLikes().size();
            }
        });
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

}
