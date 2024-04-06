package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public User createUser(User user) {
        return repository.save(user);
    }

    @Override
    public User updateUser(Map<String, String> userUpdate, Long userId) {
        return repository.update(userUpdate, userId);
    }

    @Override
    public User findUserById(Long userId) {
        return (repository.findUser(userId));
    }

    @Override
    public void deleteUserById(Long userId) {
        repository.deleteUser(userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public void checkUserExist(Long userId) {
        repository.checkUserId(userId);
    }
}