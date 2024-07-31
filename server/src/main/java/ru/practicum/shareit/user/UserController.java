package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDto addUser(@RequestBody UserDto userDto) {
        log.info("POST запрос на добавление пользователя {}", userDto);
        return userService.createUser(userDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserDto> getAllUsers() {
        log.info("GET запрос на получение списка всех пользователей.");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable Long userId) {
        log.info("GET запрос на получение пользователя с id=" + userId + ".");
        return userService.findById(userId);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH, consumes =
            MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@RequestBody Map<String, String> userUpdate, @PathVariable Long userId) {
        log.info("PATCH запрос на обновление пользователя с id=" + userId + ".");
        return userService.updateUser(userUpdate, userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long userId) {
        log.info("DELETE запрос на удаление пользователя с id=" + userId + ".");
        userService.deleteUserById(userId);
    }
}

