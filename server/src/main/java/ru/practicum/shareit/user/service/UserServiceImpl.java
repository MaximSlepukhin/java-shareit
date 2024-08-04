package ru.practicum.shareit.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.userExceptions.UserEmailException;
import ru.practicum.shareit.user.userExceptions.UserNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        try {
            user = repository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserEmailException("Пользователь с " + userDto.getEmail() + " существует");
        }
        log.debug("Новый пользователь добавлен.");
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(Map<String, String> userUpdate, Long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id=" + userId + " не найден."));

        for (Map.Entry<String, String> entry : userUpdate.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "name":
                    user.setName(value);
                    break;
                case "email":
                    user.setEmail(value);
            }
        }
        try {
            repository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserEmailException("Пользователь с " + user.getEmail() + " существует");
        }
        log.debug("Данные пользователя с id=" + userId + " обновлены.");
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto findById(Long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id=" + userId + " не найден."));
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUserById(Long userId) {
        log.debug("Пользователь с id=" + userId + " удален.");
        repository.deleteById(userId);
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        List<User> listOfUsers = repository.findAll();
        log.debug("Сформирован список всех пользователей.");
        return listOfUsers.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}