package ru.yandex.practicum.filmorate.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
        String sql = "SELECT u.*, listagg(f.friend_id) as friend_id " +
                "FROM users as u " +
                "LEFT JOIN FRIENDS F on u.user_id = F.user_id " +
                "WHERE u.user_id = ? " +
                "GROUP BY u.user_id ";
        List<User> users = jdbcTemplate.query(sql, UserDbStorage::makeUser, id);
        if (users.size() < 1) {
            throw new UserNotFoundException("Пользователя с " + id + " не существует");
        }
        return users.get(0);
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT u.*, listagg(f.friend_id) as friend_id " +
                "FROM users as u " +
                "LEFT JOIN FRIENDS F on u.user_id = F.user_id " +
                "GROUP BY u.user_id ";
        List<User> users = jdbcTemplate.query(sql, UserDbStorage::makeUser);
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
        Map<Integer, User> allUsers = new HashMap<>();
        String sql = "SELECT u.*, listagg(f.friend_id) as friend_id " +
                "FROM users as u " +
                "LEFT JOIN FRIENDS F on u.user_id = F.user_id " +
                "GROUP BY u.user_id ";
        List<User> users = jdbcTemplate.query(sql, UserDbStorage::makeUser);
        for (User user : users) {
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

    private static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("user_id"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .friends(makeFriendsList(rs))
                .build();
    }

    private static Set<Integer> makeFriendsList(ResultSet rs) throws SQLException {
        Set<Integer> friends = new HashSet<>();
        if (rs.getString("friend_id") == null) {
            return new HashSet<>();
        }
        String[] friendsLines = rs.getString("friend_id").split(",");
        for (String friendsLine : friendsLines) {
            friends.add(Integer.parseInt(friendsLine));
        }
        return friends;
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
