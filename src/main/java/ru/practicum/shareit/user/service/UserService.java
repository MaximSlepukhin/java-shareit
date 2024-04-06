package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Map;

public interface UserService {

    User createUser(User user);

    User updateUser(Map<String, String> userUpdate, Long userId);

    User findUserById(Long userId);

    void deleteUserById(Long userId);

    Collection<User> getAllUsers();

    void checkUserExist(Long userId);

}