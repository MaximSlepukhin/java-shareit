package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.userExceptions.UserDuplicateEmailException;
import ru.practicum.shareit.user.userExceptions.UserNotFoundException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private Long initialUserId = 1L;
    private final Map<Long, User> users = new TreeMap<>();

    @Override
    public User save(User user) {
        checkUser(user);
        user.setId(initialUserId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(Map<String, String> userUpdate, Long userId) {
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        User user = users.get(userId);
        User userForUpdate = new User(user.getId(),
                user.getName(),
                user.getEmail());

        for (Map.Entry<String, String> entry : userUpdate.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "name":
                    userForUpdate.setName(value);
                    break;
                case "email":
                    userForUpdate.setEmail(value);
                    if (user.getEmail().equals(userForUpdate.getEmail())) {
                        break;
                    } else {
                        checkUser(userForUpdate);
                        break;
                    }
            }
        }
        users.put(userId, userForUpdate);
        return userForUpdate;
    }

    @Override
    public User findUser(Long userId) {
        Collection<User> users = findAll();
        Optional<User> user = users.stream()
                .filter(user1 -> user1.getId().equals(userId))
                .findFirst();
        return user.orElse(null);
    }

    @Override
    public void deleteUser(Long userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
        } else {
            throw new UserNotFoundException("User with id=" + userId + "not found.");
        }
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public void checkUserId(Long userId) {
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException("Пользователя с id=" + userId + " не существует");
        }
    }

    public void checkUser(@Valid User user) {
        if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new UserDuplicateEmailException("Пользователь с email: " + user.getEmail() + " уже существует");
        }
    }
}