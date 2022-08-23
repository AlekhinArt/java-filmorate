package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data

public class User {
    private int id;
    private String login;
    private String email;
    private String name;
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();

    public User(int id, String login, String email, String name, LocalDate birthday) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.name = name;
        this.birthday = birthday;
    }

    public void addFriends(Integer id) {
        friends.add(id);
    }

    public void deleteFriend(Integer id) {
        friends.remove(id);
    }


}
