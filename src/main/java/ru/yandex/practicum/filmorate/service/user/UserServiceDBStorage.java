package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserServiceDBStorage implements UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserServiceDBStorage(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public void addFriend(int id, int friendId) {
        Map<Integer, User> users = userStorage.getUsers();
        if (!users.containsKey(id) || !users.containsKey(friendId)) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        }
        userStorage.addFriend(id, friendId);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        Map<Integer, User> users = userStorage.getUsers();
        if (!users.containsKey(id) || !users.containsKey(friendId)) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        }
        userStorage.deleteFriend(id, friendId);
    }

    @Override
    public Collection<User> getCommonFriend(int id, int friendId) {
        Map<Integer, User> users = userStorage.getUsers();
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

    @Override
    public Collection<User> getFriends(int id) {
        Map<Integer, User> users = userStorage.getUsers();
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

    @Override
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User getUser(int id) {
        return userStorage.getUser(id);
    }

    @Override
    public User createNewUser(User user) {
        return userStorage.createNewUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    @Override
    public void deleteUser(int id){
        userStorage.deleteUser(id);
    }

    @Override
    public void deleteAllUsers(){
        userStorage.deleteAll();
    }

}
