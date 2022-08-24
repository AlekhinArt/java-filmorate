package ru.yandex.practicum.filmorate.service.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;

@Service
public class UserService extends InMemoryUserStorage {

    public void addFriend(int id, int friendId) {
        Map<Integer, User> users = super.getUsers();
        if (!users.containsKey(id) || !users.containsKey(friendId)) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        }
        users.get(id).addFriends(friendId);
        users.get(friendId).addFriends(id);
    }

    public void deleteFriend(int id, int friendId) {
        Map<Integer, User> users = super.getUsers();
        if (!users.containsKey(id) || !users.containsKey(friendId)) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        }
        users.get(id).deleteFriend(friendId);
        users.get(friendId).deleteFriend(id);
    }

    public Collection<User> getCommonFriend(int id, int friendId) {
        Map<Integer, User> users = super.getUsers();
        if (!users.containsKey(id) || !users.containsKey(friendId)) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        }
        Set<Integer> friendsUserOne = users.get(id).getFriends();
        Set<Integer> friendsUserTwo = users.get(friendId).getFriends();
        List<User> commonFriend = new ArrayList<>();
        if (friendsUserOne == null || friendsUserTwo == null) {
            return commonFriend;
        } else {
            for (Integer ids : friendsUserOne) {
                if (friendsUserTwo.contains(ids)) {
                    commonFriend.add(users.get(ids));
                }
            }
        }
        return commonFriend;
    }

    public Collection<User> getFriends(int id) {
        Map<Integer, User> users = super.getUsers();
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        }
        Set<Integer> userFriends = users.get(id).getFriends();
        ArrayList<User> friend = new ArrayList<>();
        for (int ids : userFriends) {
            friend.add(users.get(ids));
        }
        return friend;
    }

}
