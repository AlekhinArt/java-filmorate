package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.UnCorrectIDException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    @BeforeEach
    public void beforeEach() {
        User user = User.builder()
                .name("user name")
                .login("login")
                .email("email@yandex.ru")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userStorage.createNewUser(user);
        Film film = Film.builder()
                .name("Film name")
                .description("description")
                .releaseDate(LocalDate.of(1990, 1, 15))
                .duration(120)
                .mpa(new MpaRating(1, "Комедия"))
                .build();
        filmStorage.createNewFilm(film);
    }

    @AfterEach
    public void afterEach() {
        filmStorage.deleteAll();
        userStorage.deleteAll();
    }

    @Test
    public void testFindUserById() {
        List<User> users = userStorage.getAllUsers();
        User user = users.get(0);
        Optional<User> userOptional = Optional.ofNullable(user);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user1 ->
                        assertThat(user1).hasFieldOrPropertyWithValue("id", user.getId())
                );
    }

    @Test
    public void testFindUserByIdNoCorrect() {
        final int id = -1;
        assertThrows(UnCorrectIDException.class,
                () -> userStorage.getUser(id));
    }

    @Test
    public void testDeleteAllUsers() {
        Collection<User> users = userStorage.getAllUsers();
        int usersSize = users.size();

        userStorage.deleteAll();

        users = userStorage.getAllUsers();
        int usersSizeAfterDel = users.size();

        assertNotEquals(usersSize, usersSizeAfterDel,
                "Удаление не произошло");
    }

    @Test
    public void testUpdateUser () {
        List <User> users = userStorage.getAllUsers();
        User user = users.get(0);

        user.setName("Perfect name");
        userStorage.updateUser(user);
        Optional<User> userOptional = Optional.ofNullable(user);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user1 ->
                        assertThat(user1)
                                .hasFieldOrPropertyWithValue("name", user.getName()));

        }

    @Test
    public void testFindFilmById() {
        List<Film> films = filmStorage.getAllFilms();
        Film film = films.get(0);

        Optional<Film> userOptional = Optional.ofNullable(film);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(film1 ->
                        assertThat(film1).hasFieldOrPropertyWithValue("id", film.getId())
                );
    }

    @Test
    public void testFindFilmByIdNoCorrect() {
        final int id = -1;
        assertThrows(UnCorrectIDException.class,
                () -> filmStorage.getFilm(id));
    }

    @Test
    public void testDeleteAllFilms() {
        Collection<Film> films = filmStorage.getAllFilms();
        int filmsSize = films.size();

        filmStorage.deleteAll();

        films = filmStorage.getAllFilms();
        int filmsSizeAfterDel = films.size();

        assertNotEquals(filmsSize, filmsSizeAfterDel,
                "Удаление не произошло");
    }

    @Test
    public void testUpdateFilm () {
        List <Film> films = filmStorage.getAllFilms();
        Film film = films.get(0);
        film.setName("Perfect name");
        filmStorage.updateFilm(film);
        Optional<Film> userOptional = Optional.ofNullable(film);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(film1 ->
                        assertThat(film1)
                                .hasFieldOrPropertyWithValue("name", film.getName()));

    }


}