package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Map;

public interface UserRepository {
    User save(User user);

    User update(Map<String, String> userUpdate, Long userId);

    User findUser(Long userId);

    void deleteUser(Long userId);

    Collection<User> findAll();

    void checkUserId(Long userId);

}

