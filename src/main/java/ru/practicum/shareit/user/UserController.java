package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.Dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto saveNewUser(@Valid @RequestBody UserDto userDto) {
        log.info("POST запрос на добавление пользователя {}", userDto);
        return userService.createUser(userDto);
    }

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        log.info("GET запрос на получение списка всех пользователей.");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        log.info("GET запрос на получение пользователя с id=" + userId + ".");
        return userService.findById(userId);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH, consumes =
            MediaType.APPLICATION_JSON_VALUE)
    public UserDto update(@RequestBody Map<String, String> userUpdate, @PathVariable Long userId) {
        log.info("PATCH запрос на обновление пользователя с id=" + userId + ".");
        return userService.updateUser(userUpdate,userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("DELETE запрос на удаление пользователя с id=" + userId + ".");
        userService.deleteUserById(userId);
    }
}

