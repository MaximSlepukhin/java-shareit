package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Map;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserDto userDto) {
        log.info("POST запрос на добавление пользователя {}", userDto);
        return userClient.createUser(userDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllUsers() {
        log.info("GET запрос на получение списка всех пользователей.");
        return userClient.getAllUsers();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {
        log.info("GET запрос на получение пользователя с id=" + userId + ".");
        return userClient.findById(userId);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH, consumes =
            MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> update(@Valid @RequestBody Map<String, String> userUpdate, @PathVariable Long userId) {
        log.info("PATCH запрос на обновление пользователя с id=" + userId + ".");
        return userClient.updateUser(userId, userUpdate);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long userId) {
        log.info("DELETE запрос на удаление пользователя с id=" + userId + ".");
        userClient.deleteUserById(userId);
    }
}
