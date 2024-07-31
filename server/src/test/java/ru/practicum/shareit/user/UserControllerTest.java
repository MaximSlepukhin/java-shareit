package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    UserDto userDtoInFirst = UserDto.builder().name("Maxim").email("user1@mail.ru").build();

    UserDto userDtoOut = new UserDto(1L, "Maxim", "user1@mail.ru");

    UserDto userDtoInSecond = UserDto.builder().name("Maxim").email("user2@mail.ru").build();

    UserDto userDtoUpdate = UserDto.builder().id(1L).name("Maxim").email("newmail@mail.ru").build();

    Map<String, String> map = new HashMap<>();

    List<UserDto> listOfUsers = new ArrayList<>(Arrays.asList(userDtoInFirst, userDtoInSecond));

    @Test
    void addUserTest() throws Exception {
        when(userService.createUser(userDtoInFirst))
                .thenReturn(userDtoOut);
        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDtoInFirst))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userDtoOut)));
    }

    @Test
    void getAllUsersTest() throws Exception {
        when(userService.getAllUsers())
                .thenReturn(listOfUsers);
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(listOfUsers)));
    }

    @Test
    void getUserByIdTest() throws Exception {
        when(userService.findById(anyLong()))
                .thenReturn(userDtoOut);
        mockMvc.perform(get("/users/" + userDtoOut.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userDtoOut)));
    }

    @Test
    void updateUserTest() throws Exception {
        when((userService.updateUser(anyMap(), anyLong())))
                .thenReturn(userDtoUpdate);
        mockMvc.perform(patch("/users/" + userDtoUpdate.getId())
                        .content(mapper.writeValueAsString(map))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userDtoUpdate)));
    }

    @Test
    void deleteUserTest() throws Exception {
        mockMvc.perform(delete("/users/" + userDtoOut.getId()))
                .andExpect(status().isOk());
        verify(userService, times(1)).deleteUserById(userDtoOut.getId());
    }
}
