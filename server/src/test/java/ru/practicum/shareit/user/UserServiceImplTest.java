package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userServiceImpl;

    UserDto userDto = UserDto.builder().name("User1").email("user1@mail.ru").build();

    UserDto userDtoOut = new UserDto(1L, "User1", "user1@mail.ru");

    User newUser = User.builder().name("User1").email("user1@mail.ru").build();

    User userFromRepository = User.builder().id(1L).name("User1").email("user1@mail.ru").build();

    User userFromRepositorySecond = User.builder().id(2L).name("User2").email("user2@mail.ru").build();

    UserDto userDtoFromRepository = UserDto.builder().id(1L).name("User1").email("user1@mail.ru").build();

    UserDto userDtoFromRepositorySecond = UserDto.builder().id(2L).name("User2").email("user2@mail.ru").build();

    UserDto userEmailUpdate = UserDto.builder().id(1L).name("User1").email("updateemail@mail.ru").build();

    UserDto userNameUpdate = UserDto.builder().id(1L).name("user1").email("user1@mail.ru").build();

    List<User> listOfUsers = new ArrayList<>(Arrays.asList(userFromRepository, userFromRepositorySecond));

    Collection<UserDto> listOfUsersDto = new ArrayList<>(Arrays.asList(userDtoFromRepository,
            userDtoFromRepositorySecond));

    static Map<String, String> mapForEmailUpdate = new HashMap<>();
    static Map<String, String> mapForNameUpdate = new HashMap<>();

    @BeforeAll
    static void createMapWithEmail() {
        mapForEmailUpdate.put("email", "updateemail@mail.ru");
    }

    @BeforeAll
    static void createMapWithName() {
        mapForNameUpdate.put("name", "user1");
    }
    //+
    @Test
    void shouldCreateUser() {
        when(userRepository.save(newUser))
                .thenReturn(userFromRepository);

        UserDto result = userServiceImpl.createUser(userDto);

        Assertions.assertEquals(result,userDtoOut);
    }
    //+
    @Test
    void shouldUpdateEmailOfUser() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(userFromRepository));

        UserDto result = userServiceImpl.updateUser(mapForEmailUpdate, 1L);

        Assertions.assertEquals(result, userEmailUpdate);
    }
    //+
    @Test
    void shouldUpdateNameOfUser() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(userFromRepository));

        UserDto result = userServiceImpl.updateUser(mapForNameUpdate, 1L);

        Assertions.assertEquals(result, userNameUpdate);
    }
    //+-
    @Test
    void shouldFindUserById() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(userFromRepository));

        UserDto result = userServiceImpl.findById(1L);

        Assertions.assertEquals(result.getId(), userFromRepository.getId());
        Assertions.assertEquals(result.getName(), userFromRepository.getName());
        Assertions.assertEquals(result.getEmail(), userFromRepository.getEmail());
    }
    //+
    @Test
    void shouldDeleteUserById() {
        userServiceImpl.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
    //+
    @Test
    void shouldGetAllUsers() {
        when(userRepository.findAll())
                .thenReturn(listOfUsers);

        Collection<UserDto> result = userServiceImpl.getAllUsers();

        Assertions.assertEquals(result, listOfUsersDto);
    }
}
