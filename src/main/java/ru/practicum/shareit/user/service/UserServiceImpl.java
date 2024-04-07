package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public User createUser(User user) {
        var newUser = repository.save(user);
        log.debug("Новый пользователь добавлен.");
        return newUser;
    }

    @Override
    public User updateUser(Map<String, String> userUpdate, Long userId) {
        var newUser = repository.update(userUpdate, userId);
        log.debug("Данные пользователя с id=" + userId + " обновлены.");
        return newUser;
    }

    @Override
    public User findUserById(Long userId) {
        var userById =  (repository.findUser(userId));
        log.debug("Пользователь с id=" + userId + " найден.");
        return userById;
    }

    @Override
    public void deleteUserById(Long userId) {
        log.debug("Пользователь с id=" + userId + " удален.");
        repository.deleteUser(userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        var listOfUsers = repository.findAll();
        log.debug("Сформирован список всех пользователей.");
        return listOfUsers;
    }

    @Override
    public void checkUserExist(Long userId) {
        repository.checkUserId(userId);
    }
}