package ru.yandex.practicum.filmorate.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import static ru.yandex.practicum.filmorate.Constants.MAX_LENGTH_DESCRIPTION;
import static ru.yandex.practicum.filmorate.Constants.MIN_RELEASE_DATE_FILM;

@Slf4j
@AllArgsConstructor
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film getFilm(int id) {
        if (id < 0) {
            throw new UnCorrectIDException("Не корректный id :" + id);
        }
        final String sql = "SELECT f.*, m.*, listagg(g.genre_id) as genre_id,listagg(g.genre_name) as genre_name " +
                "FROM films as f " +
                "LEFT JOIN mpa AS m on f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "WHERE f.film_id =?";
        List<Film> films = jdbcTemplate.query(sql, FilmDbStorage::makeFilm, id);
        return films.get(0);
    }

    @Override
    public Film createNewFilm(Film film) {
        validation(film);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO films (name, description, release_date , duration, mpa_id) VALUES ( ?, ?, ?, ?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sql, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf((film.getReleaseDate())));
            stmt.setInt(4, film.getDuration());
            if (film.getMpa() != null) {
                stmt.setInt(5, film.getMpa().getId());
            } else {
                throw new ValidationException("Не указан mpa-rating ");
            }
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        writeGenry(film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        final String sql1 = "SELECT f.*, m.*, listagg(g.genre_id) as genre_id,listagg(g.genre_name) as genre_name " +
                "FROM films as f " +
                "LEFT JOIN mpa AS m on f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "GROUP BY f.film_id " +
                "ORDER BY f.film_id ";
        final List<Film> films = jdbcTemplate.query(sql1, FilmDbStorage::makeFilm);
        if (films.size() < 1) {
            return new ArrayList<>();
        }
        return films;
    }

    @Override
    public Film updateFilm(Film film) {
        validation(film);
        String sql = "UPDATE films SET name=?,  description =?,  release_date=?," +
                " duration =? , mpa_id = ? WHERE film_id =?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        sql = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getId());
        writeGenry(film);
        return film;
    }

    public void writeGenry(Film film) {
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            Set<Genre> genres = new HashSet<>(film.getGenres());
            List<Genre> genreList = new LinkedList<>(genres);
            Collections.reverse(genreList);
            film.setGenres(genreList);
            for (Genre genre : genres) {
                String sql = "INSERT INTO film_genre(film_id, genre_id) values (?, ?)";
                jdbcTemplate.update(sql, film.getId(), genre.getId());
            }
        }
    }

    @Override
    public Map<Integer, Film> getFilms() {
        String sql = "SELECT f.*, m.*, listagg(g.genre_id) as genre_id,listagg(g.genre_name) as genre_name " +
                "FROM films as f " +
                "LEFT JOIN mpa AS m on f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "GROUP BY f.film_id " +
                "ORDER BY f.film_id ";
        List<Film> films = jdbcTemplate.query(sql, FilmDbStorage::makeFilm);
        Map<Integer, Film> allFilmWithID = new HashMap<>();
        for (Film film : films) {
            allFilmWithID.put(film.getId(), film);
        }
        return allFilmWithID;
    }

    @Override
    public void addLike(Integer idFilm, Integer idUser) {
        String sql = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, idFilm, idUser);
    }

    @Override
    public void deleteLike(Integer idFilm, Integer idUser) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, idFilm, idUser);
    }

    @Override
    public Collection<Film> getMostPopularFilms(int count) {
        String sql = "SELECT f.*, m.mpa_name, listagg(g.genre_id) as genre_id,listagg(g.genre_name) as genre_name " +
                "FROM films AS f " +
                "LEFT JOIN mpa AS m ON f.mpa_id =m.mpa_id " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT (l.user_id) DESC " +
                "LIMIT ?";
        List<Film> films = jdbcTemplate.query(sql, FilmDbStorage::makeFilm, count);
        return films;
    }

    @Override
    public MpaRating getMpaRating(int id) {
        String sql = "SELECT * FROM mpa WHERE mpa_id = ?";
        List<MpaRating> ratings = jdbcTemplate.query(sql, FilmDbStorage::makeMpa, id);
        if (ratings.isEmpty()) {
            throw new MpaNotFoundException("Рейтинга с таким " + id + "- нет");
        }
        return ratings.get(0);
    }

    @Override
    public Collection<MpaRating> getMpaRatings() {
        return jdbcTemplate.query("SELECT * FROM mpa GROUP BY mpa_id ORDER BY mpa_id ", FilmDbStorage::makeMpa);
    }

    @Override
    public Genre getGenre(int id) {
        String sql = "SELECT * FROM genre WHERE genre_id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, FilmDbStorage::makeGenre, id);
        if (genres.isEmpty()) {
            throw new GenreNotFoundException("Жанра с таким " + id + "- нет");
        }
        return genres.get(0);
    }

    @Override
    public Collection<Genre> getGenres() {
        return jdbcTemplate.query("SELECT * FROM genre GROUP BY genre_id ORDER BY genre_id",
                FilmDbStorage::makeGenre);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM films");
    }

    @Override
    public void deleteFilm(int id) {
        jdbcTemplate.update("DELETE FROM films WHERE film_id = ?", id);

    }

    private static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(new MpaRating(rs.getInt("mpa_id"), rs.getString("mpa_name")))
                .genres(makeGenres(rs))
                .build();
    }

    private static List<Genre> makeGenres(ResultSet rs) throws SQLException {
        List<Genre> genres = new LinkedList<>();
        if (rs.getString("genre_id") == null & rs.getString("genre_name") == null) {
            return new LinkedList<>();
        }
        String[] idsLines = rs.getString("genre_id").split(",");
        String[] namesLines = rs.getString("genre_name").split(",");
        for (int i = 0; i < idsLines.length; i++) {
            genres.add(new Genre(Integer.parseInt(idsLines[i]), namesLines[i]));
        }
        Collections.reverse(genres);
        return genres;
    }

    private static MpaRating makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new MpaRating(rs.getInt("mpa_id"), rs.getString("mpa_name"));
    }

    private static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
    }

    protected static void validation(Film film) {
        if (film.getId() < 0) {
            log.debug("Id-фильма {} не прошло валидацию, пустое или null или меньше нуля", film.getId());
            throw new FilmNotFoundException("Название фильма не может быть пустым");
        }
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
