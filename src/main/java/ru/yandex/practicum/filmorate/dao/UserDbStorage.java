package ru.yandex.practicum.filmorate.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UnCorrectIDException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@AllArgsConstructor
@Component("userStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(int id, int friendId) {
        String sql = "INSERT INTO friends (user_id, friend_id) VALUES ( ?, ?)";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        String sql = "DELETE FROM friends WHERE user_id =? and friend_id =?";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public User getUser(int id) {
        if (id < 1) {
            throw new UnCorrectIDException("Не корректный id: " + id);
        }
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id =?", id);
        if (userRows.next()) {
            User user = new User(
                    userRows.getInt("user_id"),
                    userRows.getString("login"),
                    userRows.getString("email"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate()
            );
            String sql = ("SELECT friend_id FROM friends WHERE user_id =" + id);
            user.setFriends(jdbcTemplate.query(sql, UserDbStorage::extractData));
            return user;
        } else {
            throw new UserNotFoundException("Пользователя с " + id + " не существует");
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = jdbcTemplate.query("select * from users", (rs, rowNum) -> makeUser(rs));
        for (User user : users) {
            String sql = ("SELECT friend_id FROM friends WHERE user_id =" + user.getId());
            user.setFriends(jdbcTemplate.query(sql, UserDbStorage::extractData));
        }
        return users;
    }

    @Override
    public User createNewUser(User user) {
        validation(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO users (login, email, name , birthday) VALUES ( ?, ?, ?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf((user.getBirthday())));
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        validation(user);
        String sql = "UPDATE users SET login=?,  email =?,  name=?, birthday =? WHERE user_id =?";
        jdbcTemplate.update(sql, user.getLogin(), user.getEmail(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public Map<Integer, User> getUsers() {
        List<User> users = jdbcTemplate.query("select * from users", (rs, rowNum) -> makeUser(rs));
        Map<Integer, User> allUsers = new HashMap<>();
        for (User user : users) {
            String sql = ("SELECT friend_id FROM friends WHERE user_id =" + user.getId());
            user.setFriends(jdbcTemplate.query(sql, UserDbStorage::extractData));
            allUsers.put(user.getId(), user);
        }
        return allUsers;
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM users");
    }

    @Override
    public void deleteUser(int id) {
        jdbcTemplate.update("DELETE FROM users where id =?", id);
    }

    private static Set<Integer> extractData(ResultSet rs) throws SQLException {
        Set<Integer> userFriends = new HashSet<>();
        while (rs.next()) {
            userFriends.add(rs.getInt("friend_id"));
        }
        return userFriends;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        return new User(rs.getInt("user_id"),
                rs.getString("login"),
                rs.getString("email"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate());
    }

    protected static void validation(User user) {
        if (user.getId() < 0) {
            throw new UnCorrectIDException("id не может быть меньше 0");
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.debug("email {} не прошел валидацию", user.getEmail());
            throw new ValidationException("Введен не корректный email");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.debug("login {} не прошел валидацию", user.getLogin());
            throw new ValidationException("Введен не корректный логин");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Дата рождения  {} не прошла валидацию, позже настоящего времени или null", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем или null");
        }
    }

}
