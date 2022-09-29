package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.Constants.MAX_LENGTH_DESCRIPTION;
import static ru.yandex.practicum.filmorate.Constants.MIN_RELEASE_DATE_FILM;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    private int id = 1;

    @Override
    public Film getFilm(int id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Фильма с таким id нет");
        }
        return films.get(id);
    }

    @Override
    public Film createNewFilm(Film film) {
        validation(film);
        film.setId(id);
        films.put(film.getId(), film);
        id++;
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException("Фильма с таким id нет");
        }
        validation(film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }

    @Override
    public void addLike(Integer idFilm, Integer idUser) {

    }

    @Override
    public void deleteLike(Integer idFilm, Integer idUser) {

    }

    @Override
    public Collection<Film> getMostPopularFilms(int count) {

        return null;
    }

    @Override
    public MpaRating getMpaRating(int id) {
        return null;
    }

    @Override
    public Collection<MpaRating> getMpaRatings() {
        return null;
    }

    @Override
    public Genre getGenre(int id) {
        return null;
    }

    @Override
    public Collection<Genre> getGenres() {
        return null;
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteFilm(int id) {

    }

    protected static void validation(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.debug("Имя {} не прошло валидацию, пустое или null", film.getName());
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription() == null || film.getDescription().length() > MAX_LENGTH_DESCRIPTION) {
            log.debug("Описание {} не прошло валидацию, по количеству символов", film.getDescription());
            throw new ValidationException("Описание фильма больше " + MAX_LENGTH_DESCRIPTION + " символов");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(MIN_RELEASE_DATE_FILM)) {
            log.debug("Дата {} не прошла валидацию, слишком ранняя дата выхода фильма", film.getReleaseDate());
            throw new ValidationException("Фильм не может выйти раньше чем " + MIN_RELEASE_DATE_FILM);
        }
        if (film.getDuration() < 0) {
            log.debug("Длительность фильма не может быть < 0, введено {}", film.getDuration());
            throw new ValidationException("Длительность фильма не может быть отрицательной");
        }
    }


}
