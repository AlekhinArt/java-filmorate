package ru.yandex.practicum.filmorate.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UnCorrectIDException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@AllArgsConstructor
@Component("filmDbStorage2")
public class FilmDBStorage2 implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;



    @Override
    public Film getFilm(int id) {
        if (id < 0) {
            throw new UnCorrectIDException("Не корректный id :" + id);
        }
        String sql1 = "SELECT f.*,m.*, g.* " +
                "FROM films as f " +
                "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN mpa AS m on f.mpa_id = m.mpa_id " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "WHERE f.film_id = ? " +
                "GROUP BY  g.genre_id,f.film_id " +
                "ORDER BY f.film_id, g.genre_id ";
        List<Film> films = jdbcTemplate.query(sql1, FilmDBStorage2::makeFilm1, id);
//        List<Film> films = jdbcTemplate.query(sql1,(rs, rowNum) -> makeFilm1(rs, rowNum), id);
        System.out.println(films);
        Film film = films.get(0);
        return film;
    }
    private static Film makeFilm1(ResultSet rs,int rowNum) throws SQLException {
        ArrayList <Genre> genres = new ArrayList<>();
        Film film = Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(new MpaRating(rs.getInt("mpa_id"), rs.getString("mpa_name")))
                .genres(listGenry(rs, rowNum))
                .build();
        return film;
    }





    private static List<Genre> listGenry(ResultSet rs, int rowNum) throws SQLException {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
        while (rs.next()) {
            genres.add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
        }
        return genres;
    }

    //public static Film

    @Override
    public Film createNewFilm(Film film) {
        return null;
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sql1 = "SELECT f.*,m.*, g.* " +
                "FROM films as f " +
                "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN mpa AS m on f.mpa_id = m.mpa_id " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "GROUP BY  g.genre_id,f.film_id " +
                "ORDER BY f.film_id, g.genre_id ";
        List<Film> films = jdbcTemplate.query(sql1,(rs, rowNum) -> makeFilm1(rs, rowNum));
        //(rs, rowNum) -> makeFollow(rs)
        System.out.println(films);

        return films;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return null;
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
}
