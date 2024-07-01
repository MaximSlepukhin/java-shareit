package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.Dto.UserDto;

import java.util.Collection;
import java.util.Map;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto updateUser(Map<String, String> userUpdate, Long userId);

    UserDto findById(Long userId);

    void deleteUserById(Long userId);

    Collection<UserDto> getAllUsers();

}