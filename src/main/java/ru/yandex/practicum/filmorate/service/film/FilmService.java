package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@AllArgsConstructor
public class FilmService extends InMemoryFilmStorage {

    UserStorage userStorage;

    public void addLike(Integer idFilm, Integer idUser) {
        checkFilm(idFilm).addLike(checkUser(idUser).getId());
    }

    public void deleteLike(Integer idFilm, Integer idUser) {
        checkFilm(idFilm).deleteLike(checkUser(idUser).getId());
    }

    public Collection<Film> getMostPopularFilms(int count) {
        Map<Integer, Film> films = super.getFilms();
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
        Map<Integer, Film> films = super.getFilms();
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
