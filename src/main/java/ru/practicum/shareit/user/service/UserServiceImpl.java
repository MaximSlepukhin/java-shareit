package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
//import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.userExceptions.UserNotFoundException;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    //private final UserRepository repository;

    /*@Override
    @Transactional
    public User createUser(User user) {
        User newUser = repository.save(user);
        log.debug("Новый пользователь добавлен.");
        return newUser;
    }*/

    /*@Override
    public User updateUser(Map<String, String> userUpdate, Long userId) {
        var newUser = repository.update(userUpdate, userId);
        log.debug("Данные пользователя с id=" + userId + " обновлены.");
        return newUser;
    }*/

    /*@Override
    @Transactional
    public User findUserById(Long userId) {
        Optional<User> userById = repository.findById(userId);
        if (userById.isEmpty()) {
            throw new UserNotFoundException("Пользователь с id=" + userId + "не найден");
        }
        log.debug("Пользователь с id=" + userId + " найден.");
        return userById.get();
    }*/

    /*@Override
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
    }*/
}